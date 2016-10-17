import java.util.concurrent._

object Chapter7_Par {


  type Par[A] = ExecutorService => Future[A]


  object Par {
    def unit[A](a: A): Par[A] = ((es: ExecutorService) => UnitFuture(a))

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

    def fork[A](a: => Par[A]): Par[A] =
      es => es.submit(new Callable[A] {
        def call = a(es).get
      })

    def lazyUnit[A](a: => A): Par[A] = fork(unit(a))
    def run[A](s: ExecutorService)(a: Par[A]) = a(s)

    def asyncF[A, B](f: A => B): A => Par[B] = a => lazyUnit(f(a))

    def map[A, B](pa: Par[A])(f: A => B): Par[B] = map2(pa, unit(()))((a, _) => f(a))

    def sortPar(parList: Par[List[Int]]): Par[List[Int]] = map(parList)(_.sorted)

    def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = sequence(ps.map(asyncF(f)))

    def sequence[A](lp: List[Par[A]]): Par[List[A]] = es => {
      fork(lp.map(p => run(es)(p)))
    }

    def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = run(as.filter(f))

  }




  def main(args: Array[String]): Unit = {
    println("fysw");
  }
}