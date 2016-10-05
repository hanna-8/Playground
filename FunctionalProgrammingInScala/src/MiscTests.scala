import scala.annotation.tailrec

object StateStuff {

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

  //println(State.zipIndex(List('a', 'b', 'c')))
  //val l = List({println('a'); 'a'}, {println('b'); 'b'}, {println('c'); 'c'})
  //println(l.foldRight(List[Char]())(_ :: _) + " alabala ")
  //println(l.foldLeft(List[Char]())((acc, c) => acc ++ List(c)))
  //println(State.zipIndex(List.fill(100000)('a')))
  //println(flatten(List(1, 2)))
  //println(flatten(List(List(1, 1), 2, List(3, List(5, 8)))))

}


object CoNtraVariance {
  case class Foo[+A]() {
    def bar[B >: A](a: B) = ??? // Works
    //def badBar(a: A) = ???  // Does not work
  }
}


object Queens {

  def toListExt[A](ss: Stream[Stream[A]]): List[List[A]] = ss match {
    case Stream.Empty => List()
    case h #:: t => h.toList :: toListExt(t)
  }

  // Remove from st all entries that would conflict with a queen
  // placed on position p, on an nxn board.
  def trim(st: Stream[(Int, Int)], p: (Int, Int), n: Int): Stream[(Int, Int)] =  {
    st filter (x => (x._1 > p._1 && x._2 != p._2)) filter
      (x => math.abs(x._1 - p._1) != math.abs(x._2 - p._2))
  }

  // index = index of the new queen to put on the board
  // qs = queens put so far
  // ps = valid positions left after putting the qs
  // n = board size
  def place(index: Int, qs: Stream[(Int, Int)], ps: Stream[(Int, Int)], n: Int): Stream[Stream[(Int, Int)]] = index match {
    case `n` => Stream(qs)
    case i => {
      ps flatMap (p => place(i + 1, p #:: qs, trim(ps, p, n), n))
    }
  }

  def put(n: Int): List[List[(Int, Int)]] = {
    val all = (for {a <- (0 to (n - 1)); b <- (0 to (n - 1))} yield (a, b)).toStream
    toListExt(place(0, Stream[(Int, Int)](), all, n))
  }
}


object MiscTests {

  def main(args: Array[String]) : Unit = {
    println(Queens.put(4))
    //println(CoNtraVariance.Foo)
  }
}