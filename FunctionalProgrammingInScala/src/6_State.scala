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

    def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] = for {
      a <- this
      b <- sb
    } yield(f(a, b)) // flatMap(a => (sb.map(b => f(a, b))))
  }


  sealed trait Input
  case object Coin extends Input
  case object Knob extends Input

  case class Machine(locked: Boolean, candies: Int, coins: Int)

  def modifyMachine(i: Input)(m: Machine): Machine = {
    (i, m.locked, m.candies, m.coins) match {
      case (Coin, true, cand, coin) if (cand > 1) => Machine(false, cand, coin + 1)
      case (Knob, false, cand, coin) if (cand > 1) => Machine(true, cand - 1, coin)
      case _ => m
    }}

  def updateMachine_v0(i: Input): State[Machine, (Int, Int)] = State(m => {
    (i, m.locked, m.candies, m.coins) match {
      case (Coin, true, cand, coin) if (cand > 1) => ((cand, coin + 1), Machine(false, cand, coin + 1))
      case (Knob, false, cand, coin) if (cand > 1) => ((cand - 1, coin), Machine(true, cand - 1, coin))
      case _ => ((m.candies, m.coins), m)
    }})


  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = inputs match {
    case h :: t => {
      for {
        _ <- State.modify(modifyMachine(h))
        m1 <- simulateMachine(t)
      } yield (m1)
    }
    case Nil => State(m => ((m.candies, m.coins), m))
  }

  def main(args: Array[String]) : Unit = {
    val m1 = Machine(true, 5, 10)
    val (coins, candies) = simulateMachine(List(Coin, Knob, Coin, Knob, Coin, Knob, Coin, Knob)).run(m1)._1
    println("coins: " + coins + "; candies: " + candies)

  }
}
