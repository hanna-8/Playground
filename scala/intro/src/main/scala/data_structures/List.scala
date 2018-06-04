/**
  * Implement the function tail for removing the first element of a List.
  * Note that the function takes constant time.
  * What are different choices you could make in your implementation if the List is Nil?
  */

package data_structures

import scala.annotation.tailrec

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def apply[A](as: A*): List[A] = {
    @tailrec def go(acc: List[A], rest: A*) : List[A] =
      if (rest.isEmpty) acc
      else go(Cons(rest.head, acc), rest.tail: _*)

    go(Nil: List[A], as: _*)
  }

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

  /**
    * 3.5. Implement dropWhile, which removes elements from the List prefix as long as they match a predicate.
    */
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Nil => Nil
    case Cons(h, t) => {
      if (f(h)) dropWhile(t, f)
      else l
    }
  }
  def dropWhileCurried[A](l: List[A])(f: A => Boolean): List[A] = l match {
    case Nil => Nil
    case Cons(h, t) => {
      if (f(h)) dropWhileCurried(t)(f)
      else l
    }
  }

  /**
    * 3.6. Implement a function, init, that returns a List consisting of all but the last element of a List.
    * So, given List(1,2,3,4), init will return List(1,2,3).
    */
  def init[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(_, Nil) => Nil
    case Cons(h, t) => Cons(h, init(t))
  }

  def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  /**
    * 3.9. Compute the length of a list using foldRight.
    */
  def length[A](as: List[A]): Int = foldRight(as, 0)((_, l) => l + 1)

  /**
    * 3.10. Our implementation of foldRight is not tail-recursive and will result in a StackOverflowError
    * for large lists (we say it’s not stack-safe).
    * Convince yourself that this is the case, and then write another general list-recursion function,
    * foldLeft, that is tail-recursive, using the techniques we discussed in the previous chapter.
    */
  @tailrec def foldLeft[A,B](as: List[A], z: B)(f: (B, A) => B): B = as match {
    case Nil => z
    case Cons(h, t) => foldLeft(t, f(z, h))(f)
  }

  /**
    * 3.11. Write sum, product, and a function to compute the length of a list using foldLeft.
    */
  def sum(as: List[Int]) = foldLeft(as, 0)(_ + _)
  def product(as: List[Double]) = foldLeft(as, 1.0)(_ * _)
  def lengthTailRec[A](as: List[A]) = foldLeft(as, 0)((l, _) => l + 1)


}







