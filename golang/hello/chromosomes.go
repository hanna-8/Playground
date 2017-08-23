// Excange parts of two slices at given indexes.

package main

import (
	"fmt"
	"sort"
)

func swap(xs, ys []int) {
	for i, _ := range xs {
		t := xs[i]
		xs[i] = ys[i]
		ys[i] = t
	}
}

func arrange(ns []int) []int {
	if len(ns) == 0 {
		return ns
	}

	sort.Ints(ns)
	j := 1
	for i := 1; i < len(ns); i++ {
		if ns[i] == ns[i - 1] {
			continue;
		}
		ns[j] = ns[i]
		j++
	}

	return ns[:j]
}

// ns: number of slices
// xs, ys: chromosomes as slices of ints
func crossover(ns []int, xs, ys []int) ([]int, []int) {
	ns = arrange(ns)
	fmt.Println("arranged", ns)
	
  for i := 0; i < len(ns); i += 2 {
		from, to := ns[i], len(xs)
		if i + 1 < len(ns) {
			to = ns[i + 1]
		}

		fmt.Println("from, to", from, to)
		swap(xs[from:to], ys[from:to])
  }

  return xs, ys
}
