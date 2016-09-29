import scala.annotation.tailrec

object MiscTests {

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



  def main(args: Array[String]) : Unit = {
    println(State.zipIndex(List('a', 'b', 'c')))
    val l = List('a', 'b', 'c')
    println(l.foldRight(List[Char]())(_ :: _))
    println(l.foldLeft(List[Char]())((acc, c) => acc ++ List(c)))
//    println(State.zipIndex(List.fill(100000)('a')))
  }
}