// Build an esolang interpreter for MiniStringFuck
// + - Increment the memory cell. If it reaches 256, wrap to 0.
// . - Output the ASCII value of the memory cell

package main

import (
	"fmt"
	"regexp"
	"strings"
)

func cleanup(code string) string {
	reg := regexp.MustCompile("[^.+]*")
	clean := reg.ReplaceAllString(code, "")
	
	return clean
}

func charInterpreter(charCode string) func(string) byte {	
	var b byte = 0
	
	return func (charCode string) byte {
		for _ = range charCode {
			b++
		}
		return b
	}
}

func Interpreter(code string) string {
	code = cleanup(code)
	parts := strings.Split(code, ".")
	ch := charInterpreter("")

	parts = parts[:len(parts) - 1]
	res := make([]byte, len(parts))
	
	fmt.Println("parts:", parts)

	for i, v := range parts {
		res[i] = ch(v)
	}
	
	fmt.Println("res:", res)

	return string(res)
}


