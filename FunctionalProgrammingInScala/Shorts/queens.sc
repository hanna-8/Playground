// Trim the remaining valid positions after placing
// a new queen on the new position np.
// I.e. remove from vp those positions what would attack np:
// the line, column and diagonals.
def trim(vp: Seq[(Int, Int)], np: (Int, Int), n: Int): Seq[(Int, Int)] =  {
  vp filter (p => p._1 > np._1 && p._2 != np._2) filter
    (p => math.abs(p._1 - np._1) != math.abs(p._2 - np._2))
}

// i = index of the new queen to put on the board
// qs = queens put so far
// ps = valid positions left for the next queens
// n = board size
def place(i: Int, qs: Seq[(Int, Int)], vp: Seq[(Int, Int)], n: Int): Seq[Seq[(Int, Int)]] = i match {
  case `n` => Seq(qs)
  case v =>
    vp flatMap (p => place(v + 1, p +: qs, trim(vp, p, n), n))
}

def put(n: Int): Seq[Seq[(Int, Int)]] = {
  val all = for {
    a <- 0 until n
    b <- 0 until n
  } yield (a, b)

  place(0, Seq[(Int, Int)](), all, n)
}

put(4)