case class Caretaker[+A]() {
  //def feed(p: A) = ??? // Ka-Boom
  def feed[B >: A](p: B) = ??? // Fixed
}