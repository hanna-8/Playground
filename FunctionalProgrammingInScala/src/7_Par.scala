/**
  * Algebra = functions operating over some data type(s)
  *         + set of laws specifying relationships between the functions.
  */

import java.util.Calendar
import java.util.concurrent._
import language.implicitConversions

object ch7_par {


  type Par[A] = ExecutorService => Future[A]


  object Par {
    def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

    private case class UnitFuture[A](get: A) extends Future[A] {
      def isDone = true
      def get(timeout: Long, units: TimeUnit) = get
      def isCancelled = false
      def cancel(evenIfRunning: Boolean): Boolean = false
    }

    def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
      (es: ExecutorService) => {
        val af = a(es)
        val bf = b(es)
        UnitFuture(f(af.get, bf.get))
      }

    def fork[A](pa: => Par[A]): Par[A] =
      es => es.submit(new Callable[A] {
        def call = pa(es).get
      })

    def lazyUnit[A](a: => A): Par[A] = fork(unit(a))
    def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

    def asyncF[A, B](f: A => B): A => Par[B] = a => lazyUnit(f(a))

    def map[A, B](pa: Par[A])(f: A => B): Par[B] = map2(pa, unit(()))((a, _) => f(a))

    def sortPar(parList: Par[List[Int]]): Par[List[Int]] = map(parList)(_.sorted)

    def sequence[A](ps: List[Par[A]]): Par[List[A]] =
      ps.foldRight[Par[List[A]]](Par.unit(List[A]()))((p, acc) => map2(p, acc)(_ :: _))

    def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = {
      val fbs: List[Par[B]] = ps.map(asyncF(f))
      sequence(fbs)
    }
//
//    def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = {
//      map(parMap(as)(asyncF[A, List[A]](a => if (f(a)) List(a) else List())))(_.flatten)
//    }
  }


  // Allow implicit conversion from Par to Par Operations,
  // in order to use infix syntax like x.map2(y)(f)

  implicit def toParOps[A](p: Par[A]): ParOps[A] = new ParOps(p)

  class ParOps[A](p: Par[A]) {
    def map2[B, C](pb: Par[B])(f: (A, B) => C): Par[C] = Par.map2(p, pb)(f)
  }


  def sum_v0(ints: IndexedSeq[Int]): Int =
    if (ints.size <= 1)
      ints.headOption getOrElse(0)
    else {
      val (l, r) = ints.splitAt(ints.size / 2)
      sum_v0(l) + sum_v0(r)
    }

  def sum_v1(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.size <= 1)
      Par.unit(ints.headOption getOrElse(0))
    else {
      val (l, r) = ints.splitAt(ints.size / 2)
      Par.map2(Par.fork(sum_v1(l)), Par.fork(sum_v1(r)))(_ + _)
    }

  // Using implicit conversions
  def sum(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.size <= 1)
      Par.unit(ints.headOption getOrElse(0))
    else {
      val (l, r) = ints.splitAt(ints.size / 2)
      Par.fork(sum_v1(l)).map2(Par.fork(sum_v1(r)))(_ + _)
    }


  def main(args: Array[String]): Unit = {
    val pool = Executors.newWorkStealingPool()

    val t1 = Calendar.getInstance().get(Calendar.MILLISECOND)
    println(Par.run(pool)(sum(IndexedSeq.range(1, 100))))

    val t2 = Calendar.getInstance().get(Calendar.MILLISECOND)
    println(sum_v0(IndexedSeq.range(1, 100)))

    val t3 = Calendar.getInstance().get(Calendar.MILLISECOND)

    println((t2 - t1) + " vs. " + (t3 - t2))

    println(Par.run(pool)(Par.parMap((1 until 100).toList)((i: Int) => print(i + " "))))
    //println(Par.run(pool)(Par.parFilter((1 until 100).toList)((i: Int) => i % 2 == 0)))
  }
}