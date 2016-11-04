import scala.util

object Chapter8_Test {

  object Gen {
    def listOf[A](g: Gen[A]): Gen[List[A]] = ???
  }

  sealed trait Gen[A]


  object Prop {
    type SuccessCount = Int   // type alias

    def forAll[A](g: Gen[A])(f: A => Boolean): Prop = ???
      //def check: String = if (f (a) == true) "OK" else "!"

  }

  sealed trait Prop {
    def check: Boolean = ???
    def && (p2: Prop): Prop = new Prop { override def check = this.check && p2.check }
  }



  def main(args: Array[String]): Unit = {
    //println(ForAll(5)((i:Int) => i % 2 == 0).check)



//    val intList = Gen.listOf(Gen.choose(0,100))
//    val prop =
//      forAll(intList)(ns => ns.reverse.reverse == ns) &&
//        forAll(intList)(ns => ns.headOption == ns.reverse.lastOption)
  }

}