package Part1

object Chapter4_Option {

  sealed trait Option[+A] {
    def map[B](f: A => B): Option[B] = this match {
      case None => None
      case Some(a) => Some(f(a))
    }

    def getOrElse[S >: A](default: => S): S = this match {
      case None => default
      case Some(a) => a
    }

    def flatMap[B](f: A => Option[B]): Option[B] = {
      map(f) getOrElse None
    }

    def orElse[B >: A](default: => Option[B]): Option[B] = {
      (this map (o => Some(o))) getOrElse default
    }

    def filter(f: A => Boolean): Option[A] = {
      this flatMap(a => if (f(a)) Some(a) else None)
    }
  }

  case class Some[+A](get: A) extends Option[A]
  case object None extends Option[Nothing]


  sealed trait Either[+E, +A]

  case class Left[+E](value: E) extends Either[E, Nothing]
  case class Right[+A](value: A) extends Either[Nothing, A]



  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  def variance(xs: Seq[Double]): Option[Double] = {
    mean(xs) flatMap (m => mean(xs.map(d => math.pow(d - m, 2))))
  }

  def map2_v0[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = (a, b) match {
    case (Some(a), Some(b)) => Some(f(a, b))
    case (_, _) => None
  }

  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    a flatMap (ao => b map (bo => f(ao, bo)))
  }

  def sequence_v0[A](a: List[Option[A]]): Option[List[A]] = a match {
    case Nil => Some(Nil)
    case h :: t => h flatMap (hv => sequence(t) map (hv :: _))
  }

  def traverse_v0[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    sequence(a map f)
  }

  def traverse_v1[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = as match {
    case Nil => Some(Nil)
    case h::t => f(h) flatMap (hv => traverse(t)(f) map (hv :: _))
  }

  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = {
    as.foldRight[Option[List[B]]](Some(Nil))((h, t) => map2(f(h), t)(_ :: _))
  }

  def sequence[A](as: List[Option[A]]): Option[List[A]] = {
    traverse(as)(o => o)
  }

  def main(args: Array[String]) : Unit = {
    println(mean(List(1, 2)))
    println(None getOrElse 1)
    println(None orElse Some(1))
    println(Some(42) orElse Some(1))
    println(Some(List()) map mean)
    println(Some(List()) flatMap mean)
    println(None map mean)
    println(None flatMap mean)
    println((None: Option[Int]) filter (i => i % 2 == 0))
    println(Option(1) filter (i => i % 2 == 0))
    println(Option(2) filter (i => i % 2 == 0))
    println(variance(List(1.0, 2.0, 5.0, 9.0)))
    println(sequence(List(Some(1), Some(2))))
    println(sequence(List(Some(1), Some(2), None, Some(3))))
    println(traverse(List(1, 2, 3, 4))(i => if (i % 2 == 0) Some(i) else None))
    println(traverse(List(2, 4))(i => if (i % 2 == 0) Some(i) else None))
  }
}
