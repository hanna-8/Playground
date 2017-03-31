(ns hello-clojure.scribbles
  (:require [clojure.core.reducers :as reducers]))


;; reduce
(defn count-letters
  ([] 0)
  ([c word]
   (+ c (count word))))

;; combine
(defn merge-counts
  ([] 0)
  ([c1 c2]
   (+ c1 c2)))

(reduce str ["a" "b" "c"])
(reduce str "z" ["a" "b" "c"])
(reducers/fold str ["a" "b" "c"])
(reducers/fold merge-counts count-letters ["a" "b" "c"])

(reduce conj #{} [:a :b :a :c :c])

;; penultimate
(#(second (reverse %)) [:d :a :b :c])

;; nth
(#(loop [current (first %1)
         remaining (rest %1)
         index %2]
    (if (= index 0)
      current
      (recur (first remaining)
             (rest remaining)
             (dec index)))) [:a :b :c] 0)

;; dec-maker
(defn dec-maker [x] #(- x %))
((dec-maker 9) 8)
(defn dec-maker1 [x] (partial + (- x)))
((dec-maker1 9) 8)

;; max
((fn [& args] (reduce #(if (< %1 %2) %2 %1) args)) 1 2 5 4 3)

;; count
(#(reduce (fn [c _] (inc c)) 0 (seq %)) "Hey")
 
;; odd numbers
(#(reverse
   (reduce
    (fn
      [acc elt]
      (if (= (mod elt 2) 0)
        acc
        (conj acc elt)))
    '()
    %))
 '(2 4))

;; or...
(filter odd? '(1 2 3 4 6 7))

;; assoc
(assoc {:a 1} :b 2 :c 3)
(assoc {:a 1 :b 2} :c 3)

;; destructure: not all collection needs to be destructured. beginning suffices.
(defn my-second
  [[_ second-thing]]
  second-thing)

(my-second ["oven" "bike" "war-axe"])
; => "bike"

;; reverse
(reduce #(conj %1 %2) '() '(1 2 3))

;; palindrome
(#(= (seq %) (reverse %)) "racecar")

;; Fibonacci
((defn Fib
   [acc n]
   (if (< 2 n)
     (Fib (cons (+ (first acc) (second acc)) acc) (dec n))
     (reverse acc)))
 [1 1] 6)

;; arrow
(-> [2 5 4 1 3 6] (reverse) (rest) (sort) (last))

;; constructsn
(conj [0 1] 2 3 4)
(cons 0 [1 2])
(into [0 1] [2 3 4])

;; flatten
(#((fn [sec acc] (if (empty? sec)
                   acc
                   (if (counted? (first sec))
                     (recur (concat (first sec) (rest sec)) acc)
                     (recur (rest sec) (conj acc (first sec))))))
   % [])
 '((1 2) 3 [4 [5 6]]))

;; capital letters
(apply str (re-seq #"[A-Z]*" "AbCDef gHi"))
()



