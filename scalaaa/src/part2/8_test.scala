import fpinscala.state._
import fpinscala.laziness._

object ch8_test {

  case class Gen[A](sample: State[RNG, A]) {
    def generate(r: RNG): A = {
      sample.run(r)._1
    }

    def flatMap[B](f: A => Gen[B]): Gen[B] = Gen(State(rng => {
      val (a, nextRng) = sample.run(rng)
      f(a).sample.run(nextRng)
    }))

    def listOfN(size: Gen[Int]): Gen[List[A]] =
      size.flatMap(i => Gen.listOfNI(i, this))

  }

  object Gen {

    def choose(start: Int, stopExclusive: Int): Gen[Int] = Gen[Int](
      State[RNG, Int](rng => {
        val (generatedNumber, nextRng) = RNG.nonNegativeInt(rng)

        (start + generatedNumber % (stopExclusive - start), nextRng)
      })
    )

    def unit[A](a: => A): Gen[A] = Gen(State.unit(a))

    def boolean: Gen[Boolean] = Gen(State(rng => RNG.boolean(rng)))

    def listOfNI[A](n: Int, g: Gen[A]): Gen[List[A]] = Gen(
      State.sequence(List.fill(n)(g.sample)))

    def union[A](g1: Gen[A], g2: Gen[A]): Gen[A] =
      boolean.flatMap {
        case true => g1
        case false => g2
      }

    // 4, 6
    // 0 -> 10
    def weighted[A](g1: (Gen[A], Int), g2: (Gen[A], Int)): Gen[A] =
      choose(0, g1._2 + g2._2).flatMap(w => if (w < g1._2) g1._1 else g2._1)

  }

  case class Prop(run: (Int, RNG) => Prop.Result) {

    import Prop._

    def check: Result = {
      val rng = RNG.Simple(8)
      val tests = 10
      run(tests, rng)
    }

    def &&(p: Prop): Prop = Prop((n, rng) =>
      run(n, rng) match {
        case Passed =>
          p.run(n, rng) match {
            case Failed(s, c) => Failed(s, c + n)
            case Passed => Passed
          }
        case r1@Failed(_, _) => r1
      })

    //def ||(p: Prop): Prop = Prop((n, rng) => {
    //}

    def &&&(p: Prop): Prop = Prop((n, rng) => {
      val (r1, r2) = (run(n, rng), p.run(n, rng))
      (r1, r2) match {
        case (Failed(s1, n1), Failed(s2, n2)) => Failed(s1 + s2, n1 + n2)
        case (Failed(s, c), _) => Failed(s, c + n)
        case (_, Failed(s, c)) => Failed(s, c + n)
        case _ => Passed
      }})

    def ||(p: Prop): Prop = Prop((n, rng) => {
      val (r1, r2) = (run(n, rng), p.run(n, rng))
      (r1, r2) match {
        case (Failed(s1, n1), Failed(s2, n2)) => Failed(s1 + s2, n1 + n2)
        case (_, _) => Passed
      }})
  }

  object Prop {

    sealed trait Result {
      def failed: Boolean
    }

    case object Passed extends Result {
      def failed = false
    }

    case class Failed(reason: String, passedCount: Int) extends Result {
      def failed = true
    }

    def forAll_0[A](ga: Gen[A])(f: A => Boolean): Prop = Prop(
      (n, rng) => {
        lazy val wholeList = Gen.listOfNI(n, ga).generate(rng)
        val nrPassed = wholeList.takeWhile(a => f(a) == true).size

        if (nrPassed == n) Passed
        else Failed("" + wholeList(nrPassed), nrPassed)
      })

    def forAll[A](as: Gen[A])(f: A => Boolean): Prop = Prop {
      (n, rng) => randomStream(as)(rng).zip(Stream.from(0)).take(n).map {
        case (a, i) => try {
          if (f(a)) Passed else Failed(a.toString, i)
        } catch {
          case e: Exception => Failed(buildMsg(a, e), i)
        }
      }.find(_.failed).getOrElse(Passed)
    }

    def randomStream[A](g: Gen[A])(rng: RNG): Stream[A] =
      Stream.unfold(rng)(rng => Some(g.sample.run(rng)))

    def buildMsg[A](s: A, e: Exception): String =
      s"test case: $s\n" +
        s"generated an exception: ${e.getMessage}\n" +
        s"stack trace:\n ${e.getStackTrace.mkString("\n")}"
  }


  def main(args: Array[String]): Unit = {
    val r = RNG.Simple(8)

    println("ybdbd " + Gen.boolean.generate(r))
    println(Gen.listOfNI(10, Gen.choose(0, 8)).generate(r))
    println(Gen.choose(0, 8).listOfN(Gen.choose(0, 10)).generate(r))

    // at most 10 numbers between 0 and 8
    val intList = Gen.choose(0, 8).listOfN(Gen.choose(0, 10))
    val prop1 = Prop.forAll(intList)(ns => ns.reverse.reverse == ns)
    val prop2 = Prop.forAll(intList)(ns => ns.size < 5)

    val prop3 = prop1 && prop2
    val prop4 = prop1 || prop2
    val prop5 = prop1 && prop1
    val prop6 = prop2 || prop2

    println("+ \t\t=> " + prop1.check)
    println("- \t\t=> " + prop2.check)
    println("+ && - \t=> " + prop3.check)
    println("+ || - \t=> " + prop4.check)
    println("+ && + \t=> " + prop5.check)
    println("- || - \t=> " + prop6.check)
  }

}