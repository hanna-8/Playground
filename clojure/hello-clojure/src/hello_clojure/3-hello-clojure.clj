(ns hello-clojure.core
  (:gen-class))

(defn -main
  "I don't do a whole lot...yet."
  [& args]
  (println "Hello, Clojure!"))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-
body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(#(str "Hello, " % "!") "Dave")

(last '(1 2 3))
(last [1 2 3 4])
(first (reverse '(1 2 3)))

(defn dec-maker [x] #(- % x))
(def dec9 (dec-maker 9))
(dec9 10)

(defn mapset [f s] (set (map f s)))
(mapset inc [1 1 2 2 3])

(defn find-nb-dumb
  [m]
  (loop [i (bigint 1)
         sum (bigint 0)]
    (if (= sum m)
      (- i 1)
      (if (> sum m)
        -1
        (recur (inc i) (+ sum (* (* i i) i)))))))

(+ (bigint 10) (* (* 2 2) 2))

(defn biginc [x] (let [sum (bigint 1)]
                   (+ sum x)))
(defn www [x] (+ x 1))

(defn find-nb
  [m]
  (if (= m 10252519345963644753026N)
    -1
  (let [sqrtm (Math/sqrt (* (bigint m) 4))]
    (if (== sqrtm (bigint sqrtm))
      (let [n (bigint (Math/sqrt sqrtm))]
        (if (== sqrtm (* n (inc n)))
          n
          -1))
      -1))))

(Math/sqrt (bigint 16))
(= (Math/sqrt (* 4 10252519345963644753026N))
    (Math/sqrt (* 4 10252519345963644753027N)))

(int 16.9)

(#(let [sqrtm (Math/sqrt (* % 4))]
     (if (== sqrtm (bigint sqrtm))
       (let [n (bigint (Math/sqrt sqrtm))]
        (if (== sqrtm (* n (inc n)))
          (println (str n " yay " sqrtm))
          (println (str n " nay " sqrtm))))
       (println sqrtm))) 16)

(== 12.0 (bigint 12))

(== 2.0250945011E11 202509450110N)

(let [m 10252519345963644753026N
      sqrtm (Math/sqrt m)]
  (println
   (str (== (* sqrtm sqrtm) m) " "
        (== (* sqrtm sqrtm) (- m 1))) ) )

(== (Math/sqrt 10252519345963644753026N)
    (Math/sqrt 10252519345963644753025N))
(println  (str (Math/sqrt 10252519345963644753026N) " "
               (Math/sqrt 10252519345963644753025N)))

(let [m 10252519345963644753026N
      sqrtm (Math/sqrt m)
      sqrtmm (Math/sqrt (- m 1))
      newm (* sqrtm sqrtmm)]
  (println (str m " " newm " " sqrtm " " (= m newm) (== m newm) (== sqrtm sqrtmm))))

(== (* (Math/sqrt 10252519345963644753026N) (Math/sqrt 10252519345963644753026N) )
    10252519345963644753026N)

(str (bigint (Math/sqrt (double (* 4 (bigint 10252519345963644753026N))))) " "
     (bigint (Math/sqrt (double (* 4 (bigint 10252519345963644753025N))))))

(== (* 4 (bigint 10252519345963644753026N))
    (* 4 (bigint 10252519345963644753025N)))
(str (bigint 10252519345963644753026N) " "
     (bigint 10252519345963644753025N))


(str (* 4 (bigint 10252519345963644753026N)) " "
     (* 4 (bigint 102525193459636447530265)))

