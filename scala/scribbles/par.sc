import java.util.concurrent._

sealed trait Par[A]

case class UnitPar[A](a: A) extends Par[A] {

}

case class Map2Par[A](es: ExecutorService) extends Par[A] {
  def run[A, B, C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] = {
    val af = UnitPar(f(pa.run(), 
  }
}

case class ForkPar[A](es: ExecutorService) extends Par[A] {

}