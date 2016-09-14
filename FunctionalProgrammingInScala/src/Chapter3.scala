import scala.annotation.tailrec 

object Chapter3 {

  sealed trait List[+A]
  case object Nil extends List[Nothing]
  case class Cons[+A](head: A, tail: List[A]) extends List[A]

  object List {
  
    def sum(ints: List[Int]): Int = ints match {
      case Nil => 0
      case Cons(x, xs) => x + sum(xs)
    }

    def product(ds: List[Double]): Double = ds match {
      case Nil => 1.0
      case Cons(0.0, _) => 0.0
      case Cons(x, xs) => x * product(xs)
    }
  
    def apply[A](as: A*): List[A] = {
      if (as.isEmpty) Nil
      else Cons(as.head, apply(as.tail: _*))
    }

    def tail[A](l: List[A]): List[A] = l match {
      case Nil => Nil
      case Cons(x, xs) => xs
    }

    def setHead[A](l: List[A], h: A): List[A] = l match {
      case Nil => Nil
      case Cons(x, xs) => Cons(h, xs)
    }

    @tailrec def drop[A](l: List[A], n: Int): List[A] = l match {
      case Nil => Nil
      case Cons(x, xs) => {
        if (n <= 0) Cons(x, xs)
        else drop(xs, n - 1)
      }
    }

    @tailrec def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
      case Nil => Nil
      case Cons(x, xs) => {
        if (!f(x)) Cons(x,xs)
        else dropWhile(xs, f)
      }
    }

    def init[A](l: List[A]) : List[A] = l match {
      case Nil => Nil
      case Cons(x, Nil) => Nil
      case Cons(x, xs) => Cons(x, init(xs))
    }

    def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B) : B = {
      as match {
        case Nil => z
        case Cons(x, xs) => f(x, foldRight(xs, z)(f))
      }
    }

    //def productFold(ns: List[Double]) = {
    //  foldRight(ns, 1.0)((x, 0.0) => 0)
                 //(x, y) => x * y)
    
    //}

    def length[C](as: List[C]): Int = {
      //foldRight(as, 0)(_ + 1)
      foldRight(as, 0)((x, y) => (1 + y))
    }

    @tailrec
    def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B) : B = {
      as match {
        case Nil => z
        case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
        //f(foldLeft(xs, z)(f), x)
      }
    }

    def sumFoldLeft(xs: List[Int]): Int = {
      foldLeft(xs, 0)(_ + _)
    }

    def prodFoldLeft(xs: List[Double]): Double = {
      foldLeft(xs, 1.0)(_ * _)
    }

    def lengthFoldLeft[A](xs: List[A]): Int = {
      foldLeft(xs, 0)((i, xs) => i + 1)
    }

    // Whyyyy?
    def reverse[A](xs: List[A]): List[A] = {
      foldLeft(xs, Nil: List[A])((r, x) => Cons(x, r) : List[A])
    }

    def foldRightLeft[A, B](as: List[A], z: B)(f: (A, B) => B) : B = {
      foldLeft(as, z)((b, a) => f(a, b))
    }

    def sumFoldRightLeft(xs: List[Int]): Int = {
      foldRightLeft(xs, 0)(_ + _)
    }

    def foldLeftRight[A, B](as: List[A], z: B)(f: (B, A) => B) : B = {
      foldRight(as, z)((a, b) => f(b, a))
    }

    def sumFoldLeftRight(xs: List[Int]): Int = {
      foldLeftRight(xs, 0)(_ + _)
    }

    def append[A](as: List[A], bs: List[A]): List[A] = {
      foldRight(as, bs)((x, xs) => Cons(x, xs))
    }
    
   
    def concat[A](ll: List[List[A]]): List[A] = {
      foldLeft(ll, Nil: List[A])(append)
    }
 
    def addOne(li: List[Int]): List[Int] = {
      foldRight(li, Nil: List[Int])((h, t) => Cons(h + 1, t))
    }
    

    def stringify(ld: List[Double]): List[String] = {
      foldRight(ld, Nil: List[String])((h, t) => Cons(h.toString, t))
    }
   
    def map[A, B](as: List[A])(f: A => B): List[B] = {
      foldRight(as, Nil: List[B])((h, t) => Cons(f(h), t))
    }

    def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = {
      foldRight(as, Nil: List[B])((h, t) => append(f(h), t))
    }

    def filter[A](as: List[A])(f: A => Boolean): List[A] = {
      //foldRight(as, Nil: List[A])((h, t) => if (f(h)) Cons(h, t) else t)
      flatMap(as)(a => if (f(a)) Cons(a, Nil) else Nil)
    }

    def addLists(l1: List[Int], l2: List[Int]): List[Int] = l1 match {
      case Nil => Nil
      case Cons(h1, t1) => l2 match {
        case Nil => Nil
        case Cons(h2, t2) => Cons(h1 + h2, addLists(t1, t2))
      }
      
    }

    def zipWith[A](l1: List[A], l2: List[A])(f: (A, A) => A): List[A] = (l1, l2) match {
      case (Nil, _) => Nil
      case (_, Nil) => Nil
      case (Cons(h1, t1), Cons(h2, t2)) => Cons(f(h1, h2), zipWith(t1, t2)(f))
    }
    
    def startsWith[A](big: List[A], small: List[A]): Boolean = (big, small) match {
      case (_, Nil) => true
      case (Nil, _) => false
      case (Cons(h1, t1), Cons(h2, t2)) => if (h1 == h2) startsWith(t1, t2) else false
    }

    def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = (sup, sub) match {
      case (Nil, _) => false
      case (_, Nil) => true
      case (Cons(_, tp), _) => {
        if (startsWith(sup, sub)) true 
        else hasSubsequence(tp, sub)
      }
    }
  }

  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  object Tree {

    def size[A](t: Tree[A]): Int = t match {
      case Leaf(a) => 1
      case Branch(l, r) => 1 + size(l) + size(r)
    }

    def maxTree(t: Tree[Int]): Int = t match {
      case Leaf(a) => a
      case Branch(l, r) => maxTree(l) max maxTree(r)
    }

    def depth[A](t: Tree[A]): Int = t match {
      case Leaf(a) => 0
      case Branch(l, r) => 1 + (depth(l) max depth(r))
    }

    def map[A, B](t: Tree[A])(f: A => B): Tree[B] = t match {
      case Leaf(a) => Leaf(f(a))
      case Branch(l, r) => Branch(map(l)(f), map(r)(f))
    }

    def fold[A, B](t: Tree[A])(lf: A => B)(bf: (B, B) => B): B = t match {
      case Leaf(a) => lf(a)
      case Branch(l, r) => bf(fold(l)(lf)(bf), fold(r)(lf)(bf))
    }

    def sizeF[A](t: Tree[A]): Int = {
      fold(t)(_ => 1)((a, b) => a + b + 1)
    }
    
    def maxTreeF(t: Tree[Int]): Int = {
      fold(t)(a => a)((x, y) => (x max y))
    }
  
    def mapF[A, B](t: Tree[A])(f: A => B): Tree[B] = {
      fold(t)(a => Leaf(f(a)): Tree[B])((x, y) => Branch(x, y))
    }

    def depthF[A](t: Tree[A]): Int = {
      fold(t)(_ => 0)((a, b) => (a max b) + 1)
    }
  }



  def main(args: Array[String]): Unit = {
    println("3's a magic number")
    println(List.tail(List("A", "B", "C")))
    println(List.setHead(List("A", "B", "C"), "D"))
    println(List.drop(List(1, 2, 3, 4, 5), 2))
    println(List.dropWhile(List(1, 2, 3, -4, -5), (x: Int) => x > 0))
    println(List.init(List(1, 2, 3, 4, 5)))
    //println(List.productFold(List(1, 2, 3)))
    println(List.foldRight(List(1, 2, 3), Nil:List[Int])(Cons(_, _)))
    println(List.length(List("A", "B", "C")))
    println(List.foldLeft(List(1, 2, 3), Nil:List[Int])((x, y) => Cons(y, x)))
    println(List.sumFoldLeft(List(1, 2, 3)))
    println(List.prodFoldLeft(List(1, 2, 3)))
    println(List.lengthFoldLeft(List(1, 2, 3)))
    println(List.reverse(List(1, 2, 3)))
    println(List.sumFoldRightLeft(List(1, 2, 3)))
    println(List.sumFoldLeftRight(List(1, 2, 3)))
    println(List.append(List(1, 2, 3), List(4, 5)))
    println(List.concat(List(List(1, 2), List(3, 4), List(4, 5))))
    println(List.addOne(List(1, 2, 3)))
    println(List.stringify(List(1.0, 2.0, 3.0)))
    println(List.map(List(1, 2, 3))(i => i.toDouble))
    println(List.filter(List(1, 2, 3, 4, 5))(i => i%2 == 0))
    println(List.flatMap(List(1, 2, 3))(i => List(i, i)))
    println(List.addLists(List(1, 2, 3), List(4, 5, 6)))
    println(List.zipWith(List(1, 2, 3), List(4, 5, 6))((i, j) => i - j))
    println(List.startsWith(List(1, 2, 3), List(1, 2, 3)))
    println(List.hasSubsequence(List(1, 2, 4, 2, 3, 5), List(2, 3)))
    println(Tree.size(Branch(Branch(Leaf("a"), Leaf("b")), Branch(Leaf("c"), Leaf("d")))))
    println(Tree.maxTree(Branch(Branch(Leaf(3), Leaf(2)), Branch(Leaf(4), Leaf(1)))))
    println(Tree.map(Branch(Branch(Leaf(3), Leaf(2)), Branch(Leaf(4), Leaf(1))))(i => i + 1))
    println(Tree.depth(Branch(Branch(Leaf(3), Leaf(2)), Branch(Branch(Leaf(4), Leaf(5)), Leaf(1)))))
    println(Tree.sizeF(Branch(Branch(Leaf("a"), Leaf("b")), Branch(Leaf("c"), Leaf("d")))))
    println(Tree.maxTreeF(Branch(Branch(Leaf(3), Leaf(2)), Branch(Leaf(4), Leaf(1)))))
    println(Tree.mapF(Branch(Branch(Leaf(3), Leaf(2)), Branch(Leaf(4), Leaf(1))))(i => i + 1))
    println(Tree.depthF(Branch(Branch(Leaf(3), Leaf(2)), Branch(Branch(Leaf(4), Leaf(5)), Leaf(1)))))
  }
}