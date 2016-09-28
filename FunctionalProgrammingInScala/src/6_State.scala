import scala.annotation.tailrec

object Chapter6_State {


  object State {
    def unit[S, A](a: A): State[S, A] = State(s => (a, s))

    def sequence[S, A, B](l: List[State[S, A]]): State[S, List[A]] = {
      l.foldRight(State.unit[S, List[A]](List[A]()))((s, acc) => s.map2(acc)(_ :: _))
    }

    def get[S]: State[S, S] = State(s => (s, s))

    def set[S](s: S): State[S, Unit] = State(_ => ((), s))

    def modify[S](f: S => S): State[S, Unit] = for {
      s <- get
      _ <- set(f(s))
    } yield()
  }


  case class State[S, +A](run: S => (A, S)) {

    def flatMap[B](g: A => State[S, B]): State[S, B] = State(s => {
      val (a, s1) = run(s)
      g(a).run(s1)
    })

    def map[B](f: A => B): State[S, B] = {
      flatMap(a => State.unit(f(a)))
    }

    def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] = {
      flatMap(a => (sb.map(b => f(a, b))))
    }
  }

  def zipIndex[A](as: List[A]): List[(Int, A)] = {
    as.foldLeft[List[(Int, A)]](List[(Int, A)]())(
      (acc, a) => acc.flatMap()
    )
  }

  sealed trait Input
  case object Coin extends Input
  case object Knob extends Input
//
//  case class Machine(locked: Boolean, candies: Int, coins: Int) {
//
//    def insertCoin: Machine = (locked, candies, coins) match {
//      case (true, cas, cos) if (cas > 0) => Machine(false, cas, cos + 1)
//      case (_, _, _) => this
//    }
//
//    def turnKnob: Machine = (locked, candies, coins) match {
//      case (false, cas, cos) if (cos > 0) => Machine(true, cas - 1, cos)
//      case (_, _, _) => this
//    }
//  }
//
//  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = for {
//
//  }
//
//    (m: Machine) => {
//    def go(ins: List[Input], acc: State[Machine, (Int, Int)]): State[Machine, (Int, Int)] = {
//      ins match {
//        case empty => acc
//        case h::t => h match {
//          case Coin => go(t, acc.modify(Machine.insertCoin))
//          case Knob => go(t, acc map turnKnob)
//        }
//      }
//    }
//
//    go(inputs, State.unit(true, 5, 10))
//  }

  def main(args: Array[String]) : Unit = {
    //val m = Machine(true, 5, 10)
    //val (coins, candies) = simulateMachine(List(Coin, Knob, Coin, Knob, Coin, Knob, Coin, Knob)).run(m)
    //println("coins: " + coins + "; candies: " + candies)
    println(zipIndex(List('a', 'b', 'c')))
  }
}
