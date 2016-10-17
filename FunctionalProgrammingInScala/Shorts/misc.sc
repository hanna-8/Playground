import scala.concurrent

def sum_0(l: Seq[Int]): Int =
  l.foldLeft(0)((i, acc) => i + acc)

def sum(ints: IndexedSeq[Int]): Int = {
  print(ints.headOption.getOrElse(0) + " ")
  if (ints.size <= 1) ints.headOption.getOrElse(0)
  else {
    val (l, r) = ints.splitAt(ints.length / 2)
    sum(l) + sum(r)
  }
}

val s = IndexedSeq(1, 2, 3, 4, 5)
sum(s)
s.splitAt(s.length / 2)._1

