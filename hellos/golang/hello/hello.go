package main

import (
	"fmt"
)

// Fibonacci closure
func fibonacci() func() int {
  x, y := 0, 1
  return func() int {

    x, y = y, x + y
    return y - x
  }
}

func main() {
	fmt.Println("= Fibonacci =")
	f := fibonacci()
  for i := 0; i < 10; i++ {
    fmt.Println(f())
  }

	fmt.Println("\n= Chromosomes =")
	xs, ys := crossover([]int {3,5,1,1,3}, []int {1,1,1,1,1,1,1}, []int {2,2,2,2,2,2,2})
  fmt.Println(xs, ys)

	fmt.Println("\n= Esolang =")
	fmt.Println(Interpreter("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+."))
	
  fmt.Printf("Go, world, go!.\n")
}
