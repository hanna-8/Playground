import scala.annotation.tailrec

object Chapter5_LazyStream {


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


  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = {
    f(z) match {
      case None => Empty
      case Some((a, s)) => Stream.cons(a, unfold(s)(f))
    }
  }

  def constant[A](a: => A): Stream[A] = {
    //unfold(Empty)((s) => Some(a, s))
    unfold(a)((a) => Some(a, a))
  }

  def from(n: Int): Stream[Int] = {
    unfold(n)((n) => Some(n, n + 1))
  }

  def fibs(n1: Int, n2: Int): Stream[Int] = {
    unfold((n1, n2)){ case (n1, n2) => Some(n1, (n2, n1 + n2)) }
  }


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

    def take_v0(n: Int): Stream[A] = (this, n) match {
      case (_, 0) => Stream.empty
      case (Empty, _) => Stream.empty
      case (Cons(h, t), n) => Stream.cons(h(), t().take(n - 1))
    }

    def drop(n: Int): Stream[A] = (this, n) match {
      case (s, 0) => s
      case (Empty, _) => Stream.empty
      case (Cons(h, t), n) => t().drop(n - 1)
    }

    def takeWhile_v0(f: A => Boolean): Stream[A] = this match {
      case Empty => Stream.empty
      case Cons(h, t) => if (f(h())) Stream.cons(h(), t().takeWhile_v0(f)) else Stream.empty
    }

    def foldRight[B](z: B)(f: (A, => B) => B): B = this match {
      case Cons(h, t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }

    def forAll(p: A => Boolean): Boolean = {
      this.foldRight(true)((elt, res) => p(elt) && res)
    }

    def takeWhile_v1(f: A => Boolean): Stream[A] = {
      this.foldRight(Empty: Stream[A])((elt, res) => if (f(elt)) Stream.cons(elt, res) else Stream.empty)
    }

    def headOption_v2: Option[A] = {
      this.foldRight(None: Option[A])((e, _) => Option(e))
      //    case Empty => None
      //    case Cons(h, t) => Option(h())
    }

    def map_v0[B](f: A => B): Stream[B] = {
      this.foldRight(Empty: Stream[B])((elt, str) => Stream.cons(f(elt), str))
    }

    def filter(f: A => Boolean): Stream[A] = {
      this.foldRight(Empty: Stream[A])((elt, str) => if (f(elt)) Stream.cons(elt, str) else str)
    }

    def append[B >: A](s2: => Stream[B]): Stream[B] = {
      this.foldRight(s2)((elt, str) => Stream.cons(elt, str))
    }

    def flatMap[B](f: A => Stream[B]): Stream[B] = {
      this.foldRight(Empty: Stream[B])((elt, str) => f(elt).append(str))
    }


    // Unfold ------------------------------------------------------------------------------

    def map[B](f: A => B): Stream[B] = {
      unfold(this) {
        case Empty => None
        case Cons(h, t) => Some(f(h()), t())
      }
    }

    def take(n: Int): Stream[A] = {
      unfold((this, n)) {
        case (Empty, _: Int) => None
        case (_, 0) => None
        case (Cons(h, t), n) => Some(h(), (t(), n - 1))
      }
    }

    def takeWhile(f: A => Boolean): Stream[A] = {
      unfold(this) {
        case Empty => None
        case Cons(h, t) => if (f(h())) Some(h(), t()) else None
      }
    }

    def zipWith[B, C](s: Stream[B])(f: (A, B) => C): Stream[C] = {
      unfold(this, s) {
        case (Cons(h1, t1), Cons(h2, t2)) => Some(f(h1(), h2()), (t1(), t2()))
        case _ => None
      }
    }

    def zipAll[B](s: Stream[B]): Stream[(Option[A], Option[B])] = {
      unfold(this, s) {
        case (Empty, Empty) => None
        case (Empty, Cons(h, t)) => Some((None, Some(h())), (Empty, t()))
        case (Cons(h, t), Empty) => Some((Some(h()), None), (t(), Empty))
        case (Cons(h1, t1), Cons(h2, t2)) => Some((Some(h1()), Some(h2())), (t1(), t2()))
      }
    }

    def startsWith[B >: A](part: Stream[B]): Boolean = {
      this.zipAll(part).forAll {
        case (_, None) => true
        case (Some(a), Some(b)) => a == b
        case _ => false
      }
    }

    def tails: Stream[Stream[A]] = {
      unfold(this) {
        case Empty => None
        case s => Some(s, s drop 1)
      }.append(Stream(Empty))
    }

    def hasSubsequence[B >: A](sub: Stream[B]): Boolean = {
      this.tails.filter(s => s.startsWith(sub)).take(1).headOption != None
    }

    def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] = {
      foldRight((z, Stream(z))) (
        (a, p) => {
          lazy val p1 = p
          val z1 = f(a, p1._1)
          (z1, Stream.cons(z1, p._2))
        }
      )._2
    }
  }


  case object Empty extends Stream[Nothing]
  case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]


  // Utils ------------------------------------------------------------------------------

  def constant_v0[A](a: => A): Stream[A] = {
    lazy val tail: Stream[A] = Cons(() => a, () => tail)
    tail
    // Stream.cons(a, constant)
  }

  def from_v0(n: Int): Stream[Int] = {
    Stream.cons(n, from(n + 1))
    //lazy val tail: Stream[Int] = Cons(() => n + 1, () => tail)
    //tail
  }

  def fibs_v0(n1: Int, n2: Int): Stream[Int] = {
    Stream.cons(n1, fibs(n2, n1 + n2))
  }

  def toListExt[A](ss: Stream[Stream[A]]): List[List[A]] = ss match {
    case Empty => List()
    case Cons(h, t) => h().toList :: toListExt(t())
  }

  def main(args: Array[String]) : Unit = {
//    println(Stream(1, 2, 3).headOption)
//    println(Stream(1, 2, 3).toList)
    println(Stream(1, 2, 3).take(2).toList)
    println(Stream(1, 2, 3).drop(2).toList)
    println(Stream(1, 5, 2, 3).takeWhile(i => (i % 2 != 0)).toList)
    println(Stream(1, 2, 3).forAll(i => (i % 2 != 0)))
    println(Stream(1, 2, 3).forAll(i => (i < 4)))
    println(Stream(1, 2, 3).headOption_v2)
    println(Stream(1, 2, 3).map(i => i * 2).toList)
    println(Stream(1, 2, 3, 4, 5).filter(i => i % 2 == 0).toList)
    println(Stream(1, 2, 3).append(Stream(4, 5)).toList)
    println(Stream(1, 2, 3).flatMap(i => Stream(i, i)).toList)
    println(constant(42).take(5).toList)
    println(from(42).take(5).toList)
    println(fibs(0, 1).take(10).toList)
    println(Stream(1, 2, 3).zipWith(Stream(4, 5, 6))((i, j) => i - j).toList)
    println(Stream(1, 2, 3, 4).zipAll(Stream(4, 5)).toList)
    println(Stream(1, 2, 3, 4).startsWith(Stream(1, 2)))
    println(Stream(1, 2, 3, 4).startsWith(Stream(1, 3)))
    println(Stream(1, 2, 3, 4).startsWith(Empty))
    println((Empty: Stream[Int]).startsWith(Stream(1, 2)))
    println(Stream(1, 2).startsWith(Stream(1, 2, 3, 4)))
    println(toListExt(Stream(1, 2, 3).tails))
    println(Stream(1, 2, 3, 4, 5).hasSubsequence(Stream(3, 4)))
    println(Stream(1, 2, 3, 4, 5).hasSubsequence(Stream(2, 4)))
    println(Stream(1,2,3).scanRight(0)(_ + _).toList)
  }
}