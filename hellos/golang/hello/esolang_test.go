package main

import (
	"testing"
)

func TestInterpreter(t *testing.T) {
	cases := []struct {
		in, want string
	}{
		{"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.+++++++++++++++++++++++++++++.+++++++..+++.+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.+++++++++++++++++++++++++++++++++++++++++++++++++++++++.++++++++++++++++++++++++.+++.++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.", "Hello, World!"},
		{"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"},
		{"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.\n+.", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"},
		{"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+.+","abcdefghijklmnopqrstuvwxyz"},
	}

	for _, c := range cases {
		got := Interpreter(c.in)
		if got != c.want {
			t.Error("Interpreter(%q) == %q, want = %q", c.in, got, c.want)
		}
	}
}
