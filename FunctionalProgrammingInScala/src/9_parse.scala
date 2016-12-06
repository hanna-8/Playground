import fpinscala.testing._

import scala.util.matching.Regex

object ch9_parse {

  trait Parsers [ParseError, Parser[+_]] { self =>

    implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]):
      ParserOps[String] = ParserOps(f(a))
    //    implicit def asCharParser[A](a: A)(implicit f: A => Parser[Char]):
    //      ParserOps[Char] = ParserOps(f(a))

    implicit def char(c: Char): Parser[Char] = string(c.toString) map (_.charAt(0))

    // Recognizes and returns a single string
    implicit def string(s: String): Parser[String] = ???

    implicit def operations[A](p: Parser[A]) = ParserOps[A](p)

    implicit def regex(rx: Regex): Parser[String] = ???

    def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B] = ???

    // Choose between p1 and p2. First attempt: p1. If p1 fails, choose p2.
    def or[A](p1: Parser[A], p2: => Parser[A]): Parser[A] = ???

    // map2 succeed
    def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] = n match {
      case 0 => succeed(List.empty)
      case i => p.map2(listOfN(i - 1, p))(_+:_)
    }

    // Recognize 0 or more repetitions of a-s.
    def many[A](p: Parser[A]): Parser[List[A]] =
      p.map2(p.many)(_+:_) or succeed(List.empty)

    // Recognize one or more a-s.
    def many1[A](p: Parser[A]): Parser[List[A]] =
      p.map2(p.many)(_+:_)

    // Apply f to the result of p, if successful.
    def map[A, B](p: Parser[A])(f: A => B): Parser[B] = ???

    // Always succeed with the value a.
    def succeed[A](a: A): Parser[A] = string("") map (_ => a)

    // Return a portion of the input string inspected by p if successful
    def slice[A](p: Parser[A]): Parser[String] = ???

    // Run p1 followed by p2, assuming p1 was successful
    def product[A, B](p1: Parser[A], p2: => Parser[B]): Parser[(A, B)] = ???

    def map2[A, B, C](p: Parser[A], p2: => Parser[B])(f: (A, B) => C): Parser[C] =
      (p ** p2) map (f.tupled)


    def run[A](p: Parser[A])(input: String): Either[ParseError, A] = ???

    case class ParserOps[A](p: Parser[A]) {
      def | [B>:A] (p2: Parser[B]): Parser[B] = self.or(p, p2)
      def or [B>:A] (p2: => Parser[B]): Parser[B] = self.or(p, p2)
      def many: Parser[List[A]] = self.many(p)
      def many1: Parser[List[A]] = self.many1(p)
      def map[B](f: A => B): Parser[B] = self.map(p)(f)
      def slice: Parser[String] = self.slice(p)
      def run(input: String): Either[ParseError, A] = self.run(p)(input)
      def product [B] (p2: Parser[B]): Parser[(A, B)] = self.product(p, p2)
      def map2[B, C] (p2: Parser[B])(f: (A, B) => C): Parser[C] = self.map2(p, p2)(f)
      def flatMap[B] (f: A => Parser[B]): Parser[B] = self.flatMap(p)(f)

      // TODO copy-paste busted! alias?
      def ** [B](p2: Parser[B]): Parser[(A, B)] = self.product(p, p2)
    }


    // TODO move outside trait .. maybe
    object laws {
      def equal[A](p1: Parser[A], p2: Parser[A])(in: Gen[String]): Prop =
        Prop.forAll(in)(s => run(p1)(s) == run(p2)(s))

      def mapLaw[A](p: Parser[A])(in: Gen[String]): Prop =
        equal(p, p.map(a => a))(in)

      def charLaw[A](in: Gen[Char]): Prop =
        Prop.forAll(in)(c => char(c).run(c.toString) == Right(c))

      def stringLaw[A](in: Gen[String]): Prop =
        Prop.forAll(in)(s => string(s).run(s) == Right(s))

      def orLaws(in: Gen[String]): Prop =
        Prop.forAll(in)(s => (s | "random").run(s) == Right(s)) &&
        Prop.forAll(in)(s => ("random" | s).run(s) == Right(s)) &&
        Prop.forAll(in)(s => (succeed(s) | s) == succeed(s))

      def succeedLaw[A](a: A)(in: Gen[String]): Prop =
        Prop.forAll(in)(s => succeed(a).run(s) == Right(a))

//      def productLaws[A](in: Gen[String]): Prop =
//        (p ** q) map f == (p map f) ** (q map f)
//        p ** (q ** r) == (p ** q) ** r

      def basic: Boolean =
        run(listOfN(3, "ya" | "ba"))("yabba dabba doo") == Right("yabba dabba doo") &&
        ("a" | "b").many.slice.run("abba") == Right("abba") && // TODO should work with chars
        char('a').many.slice.map(_.size).run("abba") == Right(2) &&
        char('c').many.slice.map(_.size).run("abba") == Right(0) &&  // Right!!!
        (char('a').many.slice.map(_.size) ** char('b').many1.slice.map(_.size)).run("aabb") == Right(2) && // 0 or more 'a' followed by one or more 'b'
        regex("[0..9]".r).flatMap(s => listOfN(s.toInt, "a")).run("2aa") == Right("2aa") // n followed by n 'a's
    }
  }

  def main(args: Array[String]): Unit = {

    println("ybdbd")

  }
}