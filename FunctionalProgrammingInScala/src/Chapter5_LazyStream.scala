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

  def take[A](s: Stream[A], n: Int): Stream[A] = (s, n) match {
    case (_, 0) => Stream.empty
    case (Empty, _) => Stream.empty
    case (Cons(h, t), n) => Stream.cons(h(), take(t(), n - 1))
  }

  def drop[A](s: Stream[A], n: Int): Stream[A] = (s, n) match {
    case (s, 0) => s
    case (Empty, _) => Stream.empty
    case (Cons(h, t), n) => drop(t(), n - 1)
  }

  def takeWhile_v0[A](s: Stream[A])(f: A => Boolean): Stream[A] = s match {
    case Empty => Stream.empty
    case Cons(h, t) => if (f(h())) Stream.cons(h(), takeWhile(t())(f)) else Stream.empty
  }

  def foldRight[A, B](s: Stream[A], z: B)(f: (A, => B) => B): B = s match {
    case Cons(h, t) => f(h(), foldRight(t(), z)(f))
    case _ => z
  }

  def forAll[A](s: Stream[A])(p: A => Boolean): Boolean = {
    foldRight(s, true)((elt, res) => p(elt) && res)
  }

  def takeWhile[A](s: Stream[A])(f: A => Boolean): Stream[A] = {
    foldRight(s, Empty: Stream[A])((elt, res) => if (f(elt)) Stream.cons(elt, res) else Stream.empty)
  }

  def headOption_v2[A](s: Stream[A]): Option[A] = {
    foldRight(s, None: Option[A])((e, _) => Option(e))
//    case Empty => None
//    case Cons(h, t) => Option(h())
  }

  def main(args: Array[String]) : Unit = {
//    println(Stream(1, 2, 3).headOption)
//    println(Stream(1, 2, 3).toList)
    println(take(Stream(1, 2, 3), 2).toList)
    println(drop(Stream(1, 2, 3), 2).toList)
    println(takeWhile(Stream(1, 5, 2, 3))(i => (i % 2 != 0)).toList)
    println(forAll(Stream(1, 2, 3))(i => (i % 2 != 0)))
    println(forAll(Stream(1, 2, 3))(i => (i < 4)))
    println(headOption_v2(Stream(1, 2, 3)))
  }
}



















