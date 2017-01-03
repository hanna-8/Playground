import scala.util.matching.Regex

// Floating point numbers
val rn = "[+-]?\\d+\\.?\\d*".r
rn.findAllIn("123 5 23.45").toList

val rs = "\"[a-zA-Z]*\" *: *".r
rs.findAllIn("\"abc\":  123\"def\": ").toList

val rword = "\\w+".r
rword.findAllIn("abd def\" ghi").toList