package Part1

object Chapter4_Either {

  sealed trait Either[+E, +A] {
    def map[B](f: A => B): Either[E, B] = this match {
      case Left(e) => Left(e)
      case Right(a) => Right(f(a))
    }

    def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
      case Left(e) => Left(e)
      case Right(a) => f(a)
    }

    def orElse[EE >: E, B >: A](default: => Either[EE, B]): Either[EE, B] = this match {
      case Left(e) => default
      case Right(a) => Right(a)
    }

    def map2_v0[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = {
      this flatMap (ae => b map (be => f(ae, be)))
    }

    def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = {
      for {
        ae <- this
        be <- b
      } yield f(ae, be)
    }
  }

  case class Left[+E](value: E) extends Either[E, Nothing]
  case class Right[+A](value: A) extends Either[Nothing, A]

  def Try[A](a: => A): Either[Exception, A] = {
    try (Right(a))
    catch {
      case e: Exception => Left(e)
    }
  }

  def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = es match {
    case Nil => Right(Nil)
    case h :: t => h flatMap (hr => sequence(t) map (hr :: _))
  }

  def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = as match {
    case Nil => Right(Nil)
    case h :: t => f(h) flatMap (fhr => traverse(t)(f) map (fhr :: _))
  }

  def main(args: Array[String]) : Unit = {
//    println(mean(List(1, 2)))
//    println(None getOrElse 1)
//    println(None orElse Some(1))
//    println(Some(42) orElse Some(1))
//    println(Some(List()) map mean)
//    println(Some(List()) flatMap mean)
//    println(None map mean)
//    println(None flatMap mean)
//    println((None: Option[Int]) filter (i => i % 2 == 0))
//    println(Option(1) filter (i => i % 2 == 0))
//    println(Option(2) filter (i => i % 2 == 0))
//    println(variance(List(1.0, 2.0, 5.0, 9.0)))
//    println(sequence(List(Some(1), Some(2))))
//    println(sequence(List(Some(1), Some(2), None, Some(3))))
//    println(traverse(List(1, 2, 3, 4))(i => if (i % 2 == 0) Some(i) else None))
//    println(traverse(List(2, 4))(i => if (i % 2 == 0) Some(i) else None))
  }
}
