object Chapter2 {
    def fibonacci(x: Int): Int = {
        def go(n: Int): Int = 
            if (n < 1) 0
            else if (n == 1) 1
            else go(n - 1) + go(n - 2)

        go(x)
    }

    def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
        // @annotation.tailrec
        def loop(n: Int): Boolean =
            if (n >= as.length - 1) true
            else if (ordered(as(n), as(n + 1))) loop(n + 1)
            else false
        
        loop(0)    
    }

    def curry[A, B, C](f: (A, B) => C): A => (B => C) =
        (a: A) => (b: B) => f(a, b)

    def uncurry[A, B, C](f: A => B => C): (A, B) => C =
        (a: A, b: B) => f(a)(b)

    def compose[A, B, C](f: B => C, g: A => B): A => C =
        (a: A) => f(g(a))

    def main(args: Array[String]): Unit = {
        println(fibonacci(6))
        println(isSorted(Array(1, 5, 3), (x: Int, y: Int) => x <= y))
    }
}

