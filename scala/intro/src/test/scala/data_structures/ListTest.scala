package data_structures

import org.scalatest.FlatSpec
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class ListTest extends FlatSpec {

  val list = List('f', 'u', 'n', 'c', 't', 'y')

  "tail" should "return all elements except the first one" in {
    List.tail(List(1.0, 2.1, 3.2)) should equal (List(2.1, 3.2))
  }

  "setHead" should "replace the first element" in {
    List.setHead('c', List('a', 'b')) should equal (List('c', 'b'))
  }

  "drop" should "remove a given number of elements from the beginning" in {
    List.drop(list, 2) should equal (List('n', 'c', 't', 'y'))
    List.drop(list, 6) should equal (Nil)
    List.drop(list, 7) should equal (Nil)
  }

  "dropWhile" should "remove elements that match a predicate" in {
    List.dropWhile(list, (c: Char) => c != 'c') should equal(List('c', 't', 'y'))
    List.dropWhileCurried(list)(_ != 'c') should equal(List('c', 't', 'y'))
  }

  "init" should "remove the last element" in {
    List.init(list) should equal(List('f', 'u', 'n', 'c', 't'))
  }

  "length" should "compute the length of a list and throw for huge lists" in {
    List.length(list) should equal(6)

    // foldRight is not tail recursive. The following should cause an out of memory exception.
    val hugeList = List((1 to 50000): _*)
    assertThrows[StackOverflowError] {
      List.length(hugeList)
    }
  }

  "sum, product and length, using foldLeft" should "compute correct results in a tail rec fashion" in {
    val hugeList = List((1 to 50000): _*)
    List.lengthTailRec(hugeList) //should equal (500000)
    // ha-haaa, cannot test because apparently should equal is not tail rec
    List.lengthTailRec(list) should equal(6)

    List.sum(List(1, -2, 3)) should equal (2)
    List.product(List(2.0, -3.0)) should equal (-6.0)
  }


}
