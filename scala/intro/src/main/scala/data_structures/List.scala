/**
  * Implement the function tail for removing the first element of a List.
  * Note that the function takes constant time.
  * What are different choices you could make in your implementation if the List is Nil?
  */

package data_structures

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  /**
    * 3.2. Implement the function tail for removing the first element of a List.
    * Note that the function takes constant time.
    */
  def tail[A](as: List[A]): List[A] = as match {
    case Nil => Nil
    case Cons(_, t) => t
  }

  /**
    * 3.3. Using the same idea, implement the function setHead
    * for replacing the first element of a List with a different value.
    */
  def setHead[A](h: A, as: List[A]): List[A] = as match {
    case Nil => Nil
    case Cons(_, t) => Cons(h, t)
  }

  /**
    * 3.4. Generalize tail to the function drop, which removes the first n elements from a list.
    * Note that this function takes time proportional only to the number of elements being
    * dropped—we don’t need to make a copy of the entire List.
    */
  def drop[A](l: List[A], n: Int): List[A] = (l, n) match {
    case (l, 0) => l
    case (Nil, _) => Nil
    case (Cons(_, t), n) => drop(t, n - 1)
  }
}

