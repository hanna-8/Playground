// Problem:
// Place n queens on an n x n chess board so that no two queens threaten each other.

// Trim the remaining valid positions after placing
// a new queen on the new position np.
// I.e. remove from vp those positions what would attack np:
// the line, column and diagonals.
def trim(vp: Seq[(Int, Int)], np: (Int, Int), n: Int): Seq[(Int, Int)] =  {
  vp filter (p => p._1 > np._1 && p._2 != np._2) filter
    (p => math.abs(p._1 - np._1) != math.abs(p._2 - np._2))
}

// i = index of the new queen to add on the board
// qs = queens put so far
// ps = valid positions left for the next queens
// n = board size
def addQueen(i: Int, qs: Seq[(Int, Int)], vp: Seq[(Int, Int)], n: Int): Seq[Seq[(Int, Int)]] = i match {
  case `n` => Seq(qs)
  case v =>
    vp flatMap (p => addQueen(v + 1, p +: qs, trim(vp, p, n), n)) // whoa, meta-recursiveness!
}

def putQueens(n: Int): Seq[Seq[(Int, Int)]] = {
  val all = for {
    a <- 0 until n
    b <- 0 until n
  } yield (a, b)

  addQueen(0, Seq[(Int, Int)](), all, n)
}

putQueens(4)