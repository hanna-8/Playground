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

    def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] = flatMap(a => (sb.map(b => f(a, b))))
  }


  sealed trait Input
  case object Coin extends Input
  case object Knob extends Input

  def insertCoin(m: Machine): Machine = (m.l, m.s, m.c) match {
    case (true, s, c) if (s > 1) => Machine(false, s, c + 1)
    case _ => m
  }

  def turnKnob(m: Machine): Machine = (m.l, m.s, m.c) match {
    case (false, s, c) if (s > 1) => Machine(true, s - 1, c)
    case _ => m
  }

  case class Machine(l: Boolean, s: Int, c: Int)

  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = {
    inputs.foldLeft(
      State.unit[Machine, (Int, Int)]((0 ,0))
    )((_, i) => for {
      m <- State.get
      _ <- State.set(if (i == Coin) insertCoin(m) else turnKnob(m))
      m2 <- State.get
      //m1 <- State.modify(if (i == Coin) insertCoin else turnKnob)
    } yield(m2.s, m2.c))
  }

//  def updateMachine(m: Machine, i: Input): Machine = (m, i) match {
//    case (m, _) if (m.s < 1) => m
//    case (m, Coin) if (m.l == true) => Machine(false, m.s, m.c + 1)
//    case (m, Knob) if (m.l == false) => Machine(true, m.s - 1, m.c)
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
    val m = Machine(true, 5, 10)
    val (coins, candies) = simulateMachine(List(Coin, Knob, Coin, Knob, Coin, Knob, Coin, Knob)).run(m)._1
    println("coins: " + coins + "; candies: " + candies)
  }
}
