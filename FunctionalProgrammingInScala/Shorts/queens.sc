def toListExt[A](ss: Stream[Stream[A]]): List[List[A]] = ss match {
  case Stream.Empty => List()
  case h #:: t => h.toList :: toListExt(t)
}

// Remove from st all entries that would conflict with a queen
// placed on position p, on an nxn board.
def trim(st: Stream[(Int, Int)], p: (Int, Int), n: Int): Stream[(Int, Int)] =  {
  st filter (x => (x._1 > p._1 && x._2 != p._2)) filter
    (x => math.abs(x._1 - p._1) != math.abs(x._2 - p._2))
}

// index = index of the new queen to put on the board
// qs = queens put so far
// ps = valid positions left after putting the qs
// n = board size
def place(index: Int, qs: Stream[(Int, Int)], ps: Stream[(Int, Int)], n: Int): Stream[Stream[(Int, Int)]] = index match {
  case `n` => Stream(qs)
  case i => {
    ps flatMap (p => place(i + 1, p #:: qs, trim(ps, p, n), n))
  }
}

def put(n: Int): List[List[(Int, Int)]] = {
  val all = (for {
    a <- (0 to (n - 1));
    b <- (0 to (n - 1))
  } yield (a, b)).toStream
  toListExt(place(0, Stream[(Int, Int)](), all, n))
}

put(4)