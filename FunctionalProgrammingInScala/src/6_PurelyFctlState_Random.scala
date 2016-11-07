package state

import scala.annotation.tailrec

object Chapter6_PurelyFunctionalState {

  // -------------------------------------------------------------- RNG

  trait RNG {
    def nextInt: (Int, RNG)
  }

  case class SimpleRNG(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = SimpleRNG(newSeed)
      val n = (newSeed >>> 16).toInt
      (n, nextRNG)
    }
  }

  def nonNegativeInt_v0(rng: RNG): (Int, RNG) = {
    val randInt = rng.nextInt._1
    val nextRng = rng.nextInt._2
    randInt match {
      case Int.MinValue => (0, nextRng)
      case n => if (n < 0) (-n, nextRng) else (n, nextRng)
    }
  }

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt
    (if (i < 0) -(i + 1) else i, r)
  }

  def double_v0(rng: RNG): (Double, RNG) = {
    val (i, r) = nonNegativeInt(rng)
    (i / (Int.MaxValue.toDouble + 1), r)
  }

  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, r1) = nonNegativeInt(rng)
    val (d, r2) = double(r1)
    ((i, d), r2)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val ((i, d), r) = intDouble(rng)
    ((d, i), r)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, r1) = double(rng)
    val (d2, r2) = double(r1)
    val (d3, r3) = double(r2)
    ((d1, d2, d3), r3)
  }

  def ints_v0(count: Int)(rng: RNG): (List[Int], RNG) = {
    @tailrec
    def go(index: Int, acc: (List[Int], RNG)): (List[Int], RNG) = {
      if (index == 0) acc
      else {
        val (i, r) = acc._2.nextInt
        go(index - 1, (i::acc._1, r))
      }
    }

    go(index = count, acc = (List(), rng))
  }

  // ---------------------------------------------------------- Rand

  type Rand[+A] = RNG => (A, RNG)

  def unit[A](a: A): Rand[A] = rng => (a, rng)
  def double: Rand[Double] = map(nonNegativeInt)(_ / (Int.MaxValue.toDouble + 1))
  def int: Rand[Int] = _.nextInt

  def map_v0[A, B](s: Rand[A])(f: A => B): Rand[B] = rng => {
    val (a, r) = s(rng)
    (f(a), r)
  }

  def nonNegativeEven: Rand[Int] = map(nonNegativeInt)(i => i - i % 2)

  def map2_v0[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = rng => {
    val (a, r1) = ra(rng)
    val (b, r2) = rb(r1)
    (f(a, b), r2)
  }

  def both[A, B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] = map2(ra, rb)((_, _))

  def sequence_v0[A](l: List[Rand[A]]): Rand[List[A]] = {
    @tailrec
    def go(rl: List[Rand[A]], acc: Rand[List[A]]): Rand[List[A]] = {
      rl match {
        case List() => acc
        case h::t => go(t, map2(h, acc)(_ :: _))
      }
    }

    go(rl = l, acc = unit(List[A]()))
  }

  def sequence[A](l: List[Rand[A]]): Rand[List[A]] = {
    l.foldRight(unit(List[A]()))((ra, rl) => map2(ra, rl)(_ :: _))
  }

  def ints(count: Int): Rand[List[Int]] = {
    sequence(List.fill(count)(int))
  }

  def flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B] = rng => {
    val (a, ra) = f(rng)
    g(a)(ra)
  }

  def nonNegativeLessThan(n: Int): Rand[Int] = {
    flatMap(nonNegativeInt) {
      i =>
        val mod = i % n
        if (i + (n - 1) - mod >= 0) unit(mod) else nonNegativeLessThan(n)
    }
  }

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] = {
    flatMap(s)(a => unit(f(a)))
  }

  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    flatMap(ra)(a => map(rb)(b => f(a, b)))
  }


  def main(args: Array[String]) : Unit = {
    val rng = SimpleRNG(42)
    val (n1, rng2) = rng.nextInt
    println(n1 + " - " + rng2)
    println(nonNegativeInt(rng)._1 + " " + nonNegativeInt(rng2)._1 + " "
      + nonNegativeInt(rng2.nextInt._2)._1 + " "
      + nonNegativeInt(rng2.nextInt._2.nextInt._2)._1)
    println(double(rng)._1 + " " + double(rng2)._1 + " "
      + double(rng2.nextInt._2)._1 + " " + double(rng2.nextInt._2.nextInt._2)._1)
    println(ints(5)(rng)._1)
    println(nonNegativeLessThan(1000)(rng))

  }
}