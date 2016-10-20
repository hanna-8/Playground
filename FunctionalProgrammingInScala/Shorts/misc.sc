import scala.concurrent
import java.util.Calendar

def sum_0(l: Seq[Int]): Int =
  l.foldLeft(0)((i, acc) => i + acc)

def sum(l: List[Int]): Int =
  l.aggregate(0)((acc, i) => acc + i, (_ + _))

def sum_1(ints: IndexedSeq[Int]): Int = {
  print(ints.headOption.getOrElse(0) + " ")
  if (ints.size <= 1) ints.headOption.getOrElse(0)
  else {
    val (l, r) = ints.splitAt(ints.length / 2)
    sum_1(l) + sum_1(r)
  }
}

//val s = 1 until 5 toList
val s:List[Int] = (1 until 100000).toList
val t1 = Calendar.getInstance().get(Calendar.MILLISECOND)
sum(s)
val t2 = Calendar.getInstance().get(Calendar.MILLISECOND)
t2 - t1
println()
val t3 = Calendar.getInstance().get(Calendar.MILLISECOND)
sum_0(s)
val t4 = Calendar.getInstance().get(Calendar.MILLISECOND)
t4 - t3

//val s = IndexedSeq(1, 2, 3, 4, 5)
//val s: List[Int] = 1 until 10000 toList
//sum(s)
// s.splitAt(s.length / 2)._1
