import scala.Option

sealed trait Par[A] {
  def unit[A](a: => A): Par[A] = ???
  def get[A](a:Par[A]): A = ???

}

object Par {
  def map2[A, B, C](p1: Par[A], p2: Par[B])(f: (A, B) => C): Par[C] = ???
}

def sum(ints: IndexedSeq[Int]): Int =
  if (ints.size <= 1)
    ints headOption getOrElse 0
  else {
    val (l, r) = ints.splitAt(ints.length / 2)
    val sumL: Par[Int] = Par.unit(sum(l))
    val sumR: Par[Int] = Par.unit(sum(r))
    Par.get(sumL) + Par.get(sumR)
  }