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

