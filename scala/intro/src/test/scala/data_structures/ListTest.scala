package data_structures

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class ListTest extends FlatSpec {

  val list = List("f", "u", "n", "c", "t", "y")

  "tail" should "return all elements except the first one" in {
    List.tail(List(1.0, 2.1, 3.2)) should equal (List(2.1, 3.2))
  }

  "setHead" should "replace the first element" in {
    List.setHead("c", List("a", "b")) should equal (List("c", "b"))
  }

  "drop" should "remove a given numver of elements from the beginning" in {
    List.drop(list, 2) should equal (List("n", "c", "t", "y"))
    List.drop(list, 6) should equal (Nil)
    List.drop(list, 7) should equal (Nil)
  }
}
