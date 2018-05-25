/** 2.1
  * Write a recursive function to get the nth Fibonacci number.
  * The first two Fibonacci numbers are 0 and 1.
  * The nth number is always the sum of the previous two—the sequence begins 0, 1, 1, 2, 3, 5.
  * Your definition should use a local tail-recursive function.
  * def fib(n: Int): Int */

import scala.annotation.tailrec


// Not tailrec, uses the stack, inefficient
def StackedFib(n: Int): Int =
  if (n <= 0) 0
  else if (n == 1) 1
  else Fib(n - 1) + Fib(n - 2)


def Fib(n: Int): Int = {
  if (n <= 0) return 0

  @tailrec def miniFib(i: Int, prev: Int, current: Int): Int =
    if (i == n) current
    else miniFib(i + 1, current, prev + current)

  miniFib(1, 0, 1)
}

Fib(0)
Fib(1)
Fib(2)
Fib(3)
Fib(4)
Fib(5)
