import fpinscala.testing._

import scala.util.matching.Regex


object ch9_parse {

  type ParseError = String
  type Parser[+A] = String => Either[ParseError, A]

  object ParsersImpl extends Parsers[ParseError, Parser] {

    override def string(s: String): Parser[String] =
      (input: String) =>
        if (input.startsWith(s)) Right(s)
        else Left("NOK::string:: " + input)

    override def regex(rx: Regex): Parser[String] =
      (input: String) =>
        input match {
          case `rx` => Right(input)
          case _ => Left("NOX::regex:: " + input)
        }

    override def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B] =
      (input: String) =>
        p(input) match {
          case Right(a) => f(a)(input)
          case Left(err) => Left(err)
        }

    override def or[A](p1: Parser[A], p2: => Parser[A]): Parser[A] =
      (input: String) =>
        p1(input) match {
          case Left(err) => p2(input)
          case r => r
        }

    override def run[A](p: Parser[A])(input: String): Either[ParseError, A] = p(input)

    //    def slice[A](p: Parser[A]): Parser[String] =
//      (input: String) => {
//        def loop (newInput: String): Parser[String] = {
//          p(newInput) match {
//            case Right(s) => loop(s)
//          }
//        }}


  }


  trait Parsers [ParseError, Parser[+_]] {
    self =>

    implicit def operations[A](p: Parser[A]) = ParserOps[A](p)

    //    implicit def asCharParser[A](a: A)(implicit f: A => Parser[Char]):
    //      ParserOps[Char] = ParserOps(f(a))
    implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]):
    ParserOps[String] = ParserOps(f(a))


    // "Lift"-er combinators

    implicit def char(c: Char): Parser[Char] = string(c.toString) map (_.charAt(0))

    //implicit def digit(i: Int): Parser[Int] =

    implicit def regex(rx: Regex): Parser[String] = ???

    // Recognizes and returns a single string
    implicit def string(s: String): Parser[String] = ???

    // Always succeed with the value a.
    def succeed[A](a: A): Parser[A] = string("") map (_ => a)

    def untrimmed(s: String): Parser[String] = regex(("\\s*" + s + "\\s*").r)


    def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B] = ???

    // map2 succeed
    def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] = n match {
      case 0 => succeed(List.empty)
      case i => p.map2(listOfN(i - 1, p))(_ +: _)
    }

    // Recognize 0 or more repetitions of a-s.
    def many[A](p: Parser[A]): Parser[List[A]] =
      p.map2(p.many)(_ +: _) or succeed(List.empty)

    // Recognize one or more a-s.
    def many1[A](p: Parser[A]): Parser[List[A]] =
      map2(p, p.many)(_ +: _)

    // Apply f to the result of p, if successful.
    def map[A, B](p: Parser[A])(f: A => B): Parser[B] =
      p.flatMap(a => succeed(f(a)))

    def map2[A, B, C](p1: Parser[A], p2: => Parser[B])(f: (A, B) => C): Parser[C] =
      p1.flatMap(a => p2.map(b => f(a, b)))

    def map2_v0[A, B, C](p1: Parser[A], p2: => Parser[B])(f: (A, B) => C): Parser[C] =
      (p1 ** p2) map f.tupled

    def map2_v1[A, B, C](p1: Parser[A], p2: => Parser[B])(f: (A, B) => C): Parser[C] =
      for {
        a <- p1
        b <- p2
      } yield f(a, b)

    // Choose between p1 and p2. First attempt: p1. If p1 fails, choose p2.
    def or[A](p1: Parser[A], p2: => Parser[A]): Parser[A] = ???

    // Run p1 followed by p2, assuming p1 was successful
    def product[A, B](p1: Parser[A], p2: => Parser[B]): Parser[(A, B)] =
      p1.flatMap(a => p2.map(b => (a, b)))

    def product_v0[A, B](p1: Parser[A], p2: => Parser[B]): Parser[(A, B)] =
      for {
        a <- p1
        b <- p2
      } yield (a, b)

    def run[A](p: Parser[A])(input: String): Either[ParseError, A] = ???

    // ???
    //def split(p: Parser[String], sep: String): Parser[Seq[String]] = p.map(s => s.split(sep))

    // Return a portion of the input string inspected by p if successful
    def slice[A](p: Parser[A]): Parser[String] = ???


    case class ParserOps[A](p: Parser[A]) {
      def |[B >: A](p2: Parser[B]): Parser[B] = self.or(p, p2)

      def flatMap[B](f: A => Parser[B]): Parser[B] = self.flatMap(p)(f)

      def many: Parser[List[A]] = self.many(p)

      def many1: Parser[List[A]] = self.many1(p)

      def map[B](f: A => B): Parser[B] = self.map(p)(f)

      def map2[B, C](p2: => Parser[B])(f: (A, B) => C): Parser[C] = self.map2(p, p2)(f)

      def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)

      def product[B](p2: Parser[B]): Parser[(A, B)] = self.product(p, p2)

      // TODO copy-paste busted! alias?
      def **[B](p2: Parser[B]): Parser[(A, B)] = self.product(p, p2)

      def run(input: String): Either[ParseError, A] = self.run(p)(input)

      def slice: Parser[String] = self.slice(p)
    }


    object laws {

      def basic: Boolean =
        run(listOfN(3, "ya" | "ba"))("yabba dabba doo") == Right("yabba dabba doo") &&
//          ("a" | "b").many.slice.run("abba") == Right("abba") && // TODO should work with chars
//          char('a').many.slice.map(_.size).run("abba") == Right(2) &&
//          char('c').many.slice.map(_.size).run("abba") == Right(0) && // Right!!!
//          (char('a').many.slice.map(_.size) ** char('b').many1.slice.map(_.size)).run("aabb") == Right(2) && // 0 or more 'a' followed by one or more 'b'
          regex("[0..9]".r).flatMap(s => listOfN(s.toInt, "a")).run("2aa") == Right("2aa") // 'n' followed by n 'a's

      def charLaw[A](in: Gen[Char]): Prop =
        Prop.forAll(in)(c => char(c).run(c.toString) == Right(c))

      def equal[A](p1: Parser[A], p2: Parser[A])(in: Gen[String]): Prop =
        Prop.forAll(in)(s => run(p1)(s) == run(p2)(s))

      def mapLaw[A](p: Parser[A])(in: Gen[String]): Prop =
        equal(p, p.map(a => a))(in)

      def orLaws(in: Gen[String]): Prop =
        Prop.forAll(in)(s => (s | "random").run(s) == Right(s)) &&
          Prop.forAll(in)(s => ("random" | s).run(s) == Right(s)) &&
          Prop.forAll(in)(s => (succeed(s) | s) == succeed(s))

      def stringLaw[A](in: Gen[String]): Prop =
        Prop.forAll(in)(s => string(s).run(s) == Right(s))

      def succeedLaw[A](a: A)(in: Gen[String]): Prop =
        Prop.forAll(in)(s => succeed(a).run(s) == Right(a))

      //      def productLaws[A](in: Gen[String]): Prop =
      //        (p ** q) map f == (p map f) ** (q map f)
      //        p ** (q ** r) == (p ** q) ** r
    }
  }


  /**
   * string(s): Recognizes and returns a single String
   * regex(s): Recognizes a regular expression s
   * slice(p): Returns the portion of input inspected by p if successful
   * succeed(a): Always succeeds with the value a
   * flatMap(p)(f): Runs a parser, then uses its result to select a second parser to
   *   run in sequence
   * or(p1,p2): Chooses between two parsers, first attempting p1, and then p2 if p1
   *   fails
   *
   * map(p)(f): Apply f to the result of p, if successful.
   * map2(p1, p2)(f)
   * many(p): Recognize 0 or more repetitions of a-s.
   * many1(p): Recognize one or more a-s.
   * **(p1, p2): Run p1 followed by p2, assuming p1 was successful. Return the pair of their results, if successful.
   */

  trait JAYSON
  object JAYSON {
    case object JNull extends JAYSON
    case class JNumber(get: Double) extends JAYSON
    case class JString(get: String) extends JAYSON
    case class JBool(get: Boolean) extends JAYSON
    case class JArray(get: IndexedSeq[JAYSON]) extends JAYSON
    case class JObject(get: Map[String, JAYSON]) extends JAYSON
  }

  trait jaysonParsers[Err, Parser[+_]] extends Parsers[Err, Parser] {

    // Simplified. Will recognize any character except ".
    // ((", s), ")
    def jaysonString: Parser[String] = ("\"" ** regex("[^\"]+".r) ** "\"").map(tuple => tuple._1._2)

    def boolLiteral: Parser[JAYSON] = ("true" | "false").map(s => JAYSON.JBool(s.toBoolean))
    def nullLiteral: Parser[JAYSON] = string("null").map(_ => JAYSON.JNull)
    def numberLiteral: Parser[JAYSON] = regex("[+-]?\\d*\\.?\\d+".r).map(s => JAYSON.JNumber(s.toDouble))
    def stringLiteral: Parser[JAYSON] = jaysonString.map(JAYSON.JString(_))


    def pair: Parser[(String, JAYSON)] =
      for {
        k <- jaysonString
        col <- untrimmed(":")
        v <- boolLiteral | nullLiteral | numberLiteral | stringLiteral | jaysonArray | jaysonObject
      } yield(k, v)


    def jaysonArray: Parser[JAYSON] =
      (untrimmed("[") ** untrimmed("]")).map(_ => JAYSON.JArray(IndexedSeq())) |  // Empty array
      (untrimmed("[") ** many(jaysonValue ** untrimmed(",")) ** jaysonValue ** untrimmed("]")).map({
        case (((p1, manyResult), lastValue), p2) => {
          val values = manyResult.map(p => p._1)  // a list of JAYSON values
          JAYSON.JArray(IndexedSeq(values: _*) :+ lastValue)
      }})

    def jaysonObject: Parser[JAYSON] =
      (untrimmed("{") ** untrimmed("}")).map(_ => JAYSON.JObject(Map())) |  // Empty object
      (untrimmed("{") ** many(pair ** untrimmed(",")) ** pair ** untrimmed("}")).map({
        case (((p1, manyResult), lastPair), p2) => {
          val values = manyResult.map(p => p._1)  // a list of JAYSON pairs
          JAYSON.JObject(Map(values: _*) + (lastPair._1 -> lastPair._2))
      }})


    def jaysonValue: Parser[JAYSON] = boolLiteral | nullLiteral | numberLiteral | stringLiteral | jaysonArray | jaysonObject
  }

  def jaysonParser[Err, Parser[+_]](P: jaysonParsers[Err, Parser]): Parser[JAYSON] = {
    import P._
    jaysonObject
  }


  def main(args: Array[String]): Unit = {
    import ParsersImpl._

    println(run(string("ya"))("yabba-dabba-doo!").toString)
    println(run(regex("[0..9]".r).flatMap((s: String) => listOfN(s.toInt, string("a"))))("2aa").toString)
    println(run(string("dabba") | string("yabba"))("dabba").toString)
    println(run(many(string("ab")))("abab").toString)
//    println(run(many(string("y") | string("d") | string("abba")))("yabbadabba").toString)
  }
}
