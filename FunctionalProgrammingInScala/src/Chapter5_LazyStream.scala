import scala.annotation.tailrec

object Chapter5_LazyStream {

  sealed trait Stream[+A] {

    def headOption: Option[A] = this match {
      case Empty => None
      case Cons(h, t) => Option(h())
    }

    def toList: List[A] = {
      @tailrec
      def go(s: Stream[A], acc: List[A]): List[A] = {
        s match {
          case Empty => acc
          case Cons(h, t) => go(t(), h() :: acc)
        }
      }
      go(this, List()).reverse
    }

    def take(n: Int): Stream[A] = this match {
      case Cons(h, t) if (n > 1) => cons(h(), t().take(n - 1))
      case Cons(h, _) if (n == 1) => cons(h(), empty)
      case _ => empty
    }

    def drop(n: Int): Stream[A] = this match {
      case Cons(h, t) if n > 0 => t().drop(n - 1)
      case _ => Empty
    }
  }

  case object Empty extends Stream[Nothing]
  case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

  object Stream {

    def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
      lazy val head = hd
      lazy val tail = tl
      Cons(() => head, () => tail)
    }

    def empty[A]: Stream[A] = Empty

    def apply[A](as: A*): Stream[A] = {
      if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))
    }

  }

  def main(args: Array[String]) : Unit = {
    println(Stream(1, 2, 3).headOption)
    println(Stream(1, 2, 3).toList)
    println(Stream(1, 2, 3).take(2))
  }
}