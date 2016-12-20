##Wha'?!
Sokoban-ish solver.
Based on the Amazon TechOn challenge, winter 2016 edition.

Real hidden purpose of the project: play around with C++11/14 new features.


##Decisions, decisions
###Design decisions disected

* Input data structure = matrix. Because:
  * A lot of "read"s will be performed on it. O(1) in a matrix. O(log(something)) in sorted array.
  * Don't expect the input to be very large. Actually... hmm... why not limit it to, say, 128x128 :D.

* Use exceptions instead of error codes because: [isocpp](https://isocpp.org/wiki/faq/exceptions) and [blog entry](http://www.shanekirk.com/2015/06/c-exceptions-the-good-the-bad-and-the-ugly/)

* Use ifstream instead of fstream because of... well C++ (not C) and [stackoverflow post](http://www.shanekirk.com/2015/06/c-exceptions-the-good-the-bad-and-the-ugly/)


