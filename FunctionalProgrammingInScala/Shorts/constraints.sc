// Examples in a presentation by Runar:
// https://www.youtube.com/watch?v=GqmsQeSzMdw

trait Functor[F[_]] {
  def map[A, B](f: A => B): F[A] => F[B]
}

trait Applicative[F[_]] extends Functor[F] {
  def pure[A](a: A): F[A]
  def map2[A, B, C](f: A => B): (F[A], F[B]) => F[C]
}

trait Monad[F[_]] extends Applicative[F] {
  def join[A](a: F[F[A]]): F[A]
}
// Allows flatMap (map + join)
// Allows for comprehensions (?)

def compose[F[_], G[_]] (f: Applicative[F], g: Applicative[G]): Applicative[F[G[_]]] = {
  new Applicative[F[G[_]]] {
    def pure[A](a: A) = f.pure(g.pure(a))
    def map2[A, B, C](f: (A, B) => C) = f.map2(g.map2(f))
  }
}
// Impossible with monads, needed: monad transformers


/***************************
  * Futures:
  *
  * Fork:
  *   A => Future[A]
  *
  * Map:
  *   (A => B) => (Future[A] => Future[B])    (append functions)
  *
  * Join:
  *   Future[Future[A]] => Future[A]
  *
  *** Algebraic reasoning possible :)
  *
  **************************
  * Actors:
  *
  * Any => Unit
  * Side effects => -- composability
  *
  * */