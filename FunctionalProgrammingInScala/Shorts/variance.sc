case class Variance[+A]() {
  //def aFunction(p: A) = ??? // Ka-Boom
  def aFunction[B >: A](p: B) = ??? // Fixed
}
