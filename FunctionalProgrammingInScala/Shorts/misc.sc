val c = 'b' - 'a'
val d = ('a' + 5).toChar

import scala.util.Random

val r = new Random()
r.nextInt
r.nextInt
r.nextInt

List(1, 2) :+ 3
3 :: List(1, 2)
3 +: Seq(1, 2)
Seq(1, 2) :+ 3
Seq(1, 2) ++ Seq(3, 4)
List(1, 2) ::: List(3, 4)
Map(('a', 1), ('b', 2)) + ('c', 3)

List(('a', 1), ('b', 2)).map(p => p._1)