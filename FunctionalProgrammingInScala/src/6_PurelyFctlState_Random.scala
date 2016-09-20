import scala.annotation.tailrec

object Chapter6_PurelyFunctionalState {

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

  def double(rng: RNG): (Double, RNG) = {
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

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    @tailrec
    def go(index: Int, acc: (List[Int], RNG)): (List[Int], RNG) = {
      if (index == 0) acc
      else {
        val (i, r) = acc._2.nextInt
        go(index - 1, (i::acc._1, r))
      }
    }

    go(count, (List(), rng))
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
  }
}