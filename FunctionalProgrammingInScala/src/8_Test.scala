import scala.annotation.tailrec
import scala.util.Random

object Chapter8_Test {

  object Gen {
    def choose(start: Int, stopExclusive: Int): Gen[Int] =
      new Gen(r => r.nextInt(stopExclusive - start) + start)

    def unit[A](a: => A): Gen[A] = new Gen(r => a)

    def boolean: Gen[Boolean] = new Gen(r => r.nextBoolean())

    // List of max. 42 random elements of type A
    def listOf[A](g: Gen[A]): Gen[List[A]] = g.listOfN(Gen.choose(0, 42))

    def union[A](g1: Gen[A], g2: Gen[A]): Gen[A] = boolean.flatMap(b => if(b) g1 else g2)

    def weighted[A](g1: (Gen[A], Int), g2: (Gen[A], Int)): Gen[A] = {
      choose(0, g1._2 + g2._2).flatMap[A]((w: Int) => { if (w < g1._2) g1._1 else g2._1 } )
    }
  }


  case class Gen[A](get: Random => A) {

    def listOfN(n: Int): Gen[List[A]] = new Gen(r => List.fill(n)(get(r)))

    def flatMap[B](f: A => Gen[B]): Gen[B] = new Gen(r => f(get(r)).get(r))

    def listOfN(size: Gen[Int]): Gen[List[A]] = size flatMap (n => listOfN(n))
  }



  object Prop {

    trait Result {
      def failed: Boolean
    }

    case object Passed extends Result {
      def failed = false
    }

    case class Failed(reason: FailureReason, successes: SuccessCount) extends Result {
      def failed = true
    }

    type SuccessCount = Int // type alias
    type FailureReason = String
    type TestsCount = Int

    def forAll[A](g: Gen[A])(f: A => Boolean): Prop = new Prop ((count: Int, rnd: Random) => {
      @tailrec
      def go(index: Int): Result =
        if (index == count)
          Prop.Passed
        else {
            val v = g.get(rnd)
            if (!f(v)) Prop.Failed("" + v, index)
            else go(index + 1)
        }

      go(0)
    })

  }

  case class Prop(run: (Prop.TestsCount, Random) => Prop.Result) {
    def check: Prop.Result = {
      val r: Random = new Random(42)
      val tests = 10
      run(tests, r)
    }

    def && (p2: Prop): Prop = new Prop((count, random) => {
      val c1 = check
      if (c1 != Prop.Passed)
        c1
      else
        p2.check
    }

  }

  def main(args: Array[String]): Unit = {
    //println(ForAll(5)((i:Int) => i % 2 == 0).check)
    val r: Random = new Random(42)
    println(Gen.listOf(Gen.choose(1, 42)).get(r))

    val intList = Gen.listOf(Gen.choose(0,100))
    val prop = Prop.forAll(intList)(ns => ns.reverse.reverse == ns)
    val prop1 = Prop.forAll(intList)(ns => ns.size > 5)

    println(prop.check)
    println(prop1.check)
//        forAll(intList)(ns => ns.headOption == ns.reverse.lastOption)
  }

}