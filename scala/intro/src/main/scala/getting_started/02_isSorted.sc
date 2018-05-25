/**
  * Implement isSorted, which checks whether an Array[A] is sorted
  * according to a given comparison function:
  * def isSorted[A](as: Array[A], ordered: (A,A) => Boolean): Boolean
  */

import scala.annotation.tailrec

def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
  def len = as.length

  @tailrec def check[A](i: Int): Boolean =
    if (i == len - 1) true
    else if (ordered(as(i), as(i + 1)) == false) false
    else check(i + 1)

  check(0)
}

isSorted(Array(0, 1, 2), (a: Int, b: Int) => (a < b))
isSorted(Array('a', 'c', 'b'), (c1: Char, c2: Char) => (c1 > c2))
