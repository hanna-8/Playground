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

//  def updateMachine(i: Input): State[Machine, (Int, Int)] = State(m => {
//    i match {
//      case Coin if (m.l == true && m.s > 1) ((m.s, m.c + 1), Machine(false, m.s, m.c + 1))
//      case Knob if (m.l == false && m.s > 1) ((m.s - 1, m.c), Machine(false, m.s - 1, m.c))
//      case _ => ((m.s, m.c), m)
//    }})

  def insertCoin(m: Machine): Machine = (m.locked, m.candies, m.coins) match {
    case (true, candies, coins) if (candies > 1) => Machine(false, candies, coins + 1)
    case _ => m
  }

  def turnKnob(m: Machine): Machine = (m.locked, m.candies, m.coins) match {
    case (false, candies, coins) if (candies > 1) => Machine(true, candies - 1, coins)
    case _ => m
  }

  case class Machine(locked: Boolean, candies: Int, coins: Int)


  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = inputs match {
    case Coin :: t => State(m => {
      val m1 = insertCoin(m)
      println("coin. (" + m1.candies + " " + m1.coins + ")")
      simulateMachine(t).run(m1)
    })
    case Knob :: t => State(m => {
      val m1 = turnKnob(m)
      println("knob. (" + m1.candies + " " + m1.coins + ")")
      simulateMachine(t).run(m1)
    })
    case Nil => State(m => ((m.candies, m.coins), m))
  }


//      for {
//      _ <- State.modify(if (h == Coin) insertCoin else turnKnob)
//      m1 <- State.get
//    } yield(m1.candies, m1.coins)
//
    //case Nil =>



//  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] =
//    inputs map (i => {
//      for {
//        //m <- State.get
//        //_ <- State.set(if (i == Coin) insertCoin(m) else turnKnob(m))
//        //m2 <- State.get
//        _ <- State.modify(if (i == Coin) insertCoin else turnKnob)
//        m1 <- State.get
//      } yield(m1.s, m1.c) }) last

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
    val m1 = Machine(true, 5, 10)
    val (coins, candies) = simulateMachine(List(Coin, Knob, Coin, Knob, Coin, Knob, Coin, Knob)).run(m1)._1
    println("coins: " + coins + "; candies: " + candies)

    //    val (coins1, candies1) = simulateMachine(List(Coin)).run(m1)._1
//    println("coins: " + coins1 + "; candies: " + candies1)
//
//    val m2 = Machine(true, 5, 10)
//    val (coins2, candies2) = simulateMachine(List(Coin, Knob)).run(m2)._1
//    println("coins: " + coins2 + "; candies: " + candies2)
//
//    val m = Machine(true, 5, 10)
//    val (coins, candies) = simulateMachine(List(Coin, Knob, Coin)).run(m)._1
//    println("coins: " + coins + "; candies: " + candies)
  }
}
