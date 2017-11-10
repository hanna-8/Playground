(ns hello-clojure.fp)

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(c-int character)
; => 10

(c-str character)
; => 4

(c-dex character)
; => 5

(defn attr [key] (comp key :attributes))
((attr :intelligence) character)

(defn compmoc
  ([] ())
  ([& args]
   (if (= 1 (count args))
     (first args)
     ((compmoc (butlast args)) (partial (last args))))))

((compmoc (partial apply str) reverse str) "hello" "clojuredocs")
((comp (partial apply str) reverse str) "hello" "clojuredocs")

(butlast '(1 2 3))
((fn [& args] (reverse (seq args))) 1 2 3 4)
(->> '(1 2 3) '(count inc))


(defn comp-fn
  ([] comp-fn)
  ([f & r]
   (if (seq r)
     (fn [& args] (f (apply (apply comp-fn r) args)))
     f)))

