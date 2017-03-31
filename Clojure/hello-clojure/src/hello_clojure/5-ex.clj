(ns hello-clojure.ex)

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(defn attr [atribute] (comp atribute :attributes))

(defn comp-fn2
  [f2 f1]
  (fn [& args] (f2 (apply f1 args))))

(defn comp-fn
  ([] comp-fn)
  ([f & r]
   (if (seq r)
     (fn [& args] (f (apply (apply comp-fn r) args)))
     f)))

