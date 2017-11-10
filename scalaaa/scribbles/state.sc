case class State[S, +A] (runS: S => (A, S)) {
  def map[B](f: A => B) =
    State[S, B](s => {
      val (a, s1) = runS(s)
      (f(a), s1)
    })

  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State[S, B](s => {
      val (a, s1) = runS(s)
      f(a) runS s1
    })
}


object State {
  def getState[S]: State[S, S] = State[S, S](s => (s, s))

  def setState[S](s: S): State[S, Unit] = State(_ => ((), s))

  def pureState[S, A](a: A): State[S, A] = State(s => (a, s))

  def zipIndex[A](as: List[A]): List[(Int, A)] = {
    as.foldLeft(
      pureState[Int, List[(Int, A)]](List())
    )((acc, a) => for {
      xs <- acc
      n <- getState
      u <- setState(n + 1)
    } yield((n, a)::xs)).runS(0)._1.reverse
  }
}

def flatten_v0(l: List[Any]): List[Any] = l match {
  case h :: t => h match {
    case lh: List[_] => {println(lh); flatten_v0(lh) ++ flatten_v0(t)}
    case e => {println(e); e :: flatten_v0(t)}
  }
  case Nil => List()
}

def flatten(l: List[Any]): List[Any] = l flatMap {
  case ls: List[_] => flatten(ls)
  case e => List(e)
}

State.zipIndex(List('a', 'b', 'c'))
val l = List({println('a'); 'a'}, {println('b'); 'b'}, {println('c'); 'c'})
l.foldRight(List[Char]())(_ :: _)
l.foldLeft(List[Char]())((acc, c) => acc ++ List(c))
//State.zipIndex(List.fill(100000)('a')))
flatten(List(1, 2))
flatten(List(List(1, 1), 2, List(3, List(5, 8))))
