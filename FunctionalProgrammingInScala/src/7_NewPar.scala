//import java.util.Calendar
//import java.util.concurrent._
//
//object Chapter7_NewPar {
//
//  case class UnitFuture[A](v: A) extends Future[A] {
//    def isDone = true
//    def get(timeout: Long, units: TimeUnit) = v
//    def get() = v
//    def isCancelled = false
//    def cancel(evenIfRunning: Boolean): Boolean = false
//  }
//
//
//
//
//  case class ComposableFuture[A](es: ExecutorService) extends Future[A] {
//
//    def map2(fb: ComposableFuture)(f: (A, A) => A): ComposableFuture[A] = {
//      val (a, b) = es.invokeAll(List[Callable[A]](
//        new Callable[A] { def call = get },
//        new Callable[A] { def call = fb.get }
//      )).map()
//
//      es.submit(f(a1, a2))
//    }
//  }
//
//
//
//
//
//
//
//  class NextPar[A](f: Future[A], es: ExecutorService) {
//
//    def get: A => f.get
//
//  }
//
//  class NewPar[A](es: ExecutorService) extends Future[A] {
//
//    def map[B](f: A => B): NextFuture[B] = es.submit(new Callable[B] { def call = f(get) })
//
//
//    def map2[B, C](p: NextFuture[B])(f: (A, B) => C): NextFuture[C] = {
//      val (a, b) = es.invokeAll(Seq(
//        new Callable[A] { def call = this.get },
//        new Callable[B] { def call = p.get }
//      )).map(_.get)
//
//  }
//
//  // Return Futures, combine futures with Callable {F.get}
//
//  object NewFuture[A](a: A) {
//
//
//
//      es.submit(new Callable {
//        def call = f(a, b)
//      })
//    }
//
//    def unit[A](a: A): NextFuture = UnitFuture(a)
//
//    def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
//      (es: ExecutorService) => {
//        val af = a(es)
//        val bf = b(es)
//        UnitFuture(f(af.get, bf.get))
//      }
//  }
//
//
//
//  def sum_v0(ints: IndexedSeq[Int]): Int =
//    if (ints.size <= 1)
//      ints.headOption getOrElse(0)
//    else {
//      val (l, r) = ints.splitAt(ints.size / 2)
//      sum_v0(l) + sum_v0(r)
//    }
//
//  def sum(ints: IndexedSeq[Int], es: ExecutorService): Future[Int] =
//    if (ints.size <= 100)
//      NewPar.start(es)(ints.foldLeft(0)(_ + _))
//    else {
//      val (l, r) = ints.splitAt(ints.size / 2)
//      NewPar.combine(es)(sum(l), sum(r))(_ + _)
//    }
//
//
//  def main(args: Array[String]): Unit = {
//    val pool = Executors.newWorkStealingPool()
//
//    val t1 = Calendar.getInstance().get(Calendar.MILLISECOND)
//    println(Par.run(pool)(sum(IndexedSeq.range(1, 100))))
//
//    val t2 = Calendar.getInstance().get(Calendar.MILLISECOND)
//    println(sum_v0(IndexedSeq.range(1, 100)))
//
//    val t3 = Calendar.getInstance().get(Calendar.MILLISECOND)
//
//    println((t2 - t1) + " vs. " + (t3 - t2))
//  }
//}