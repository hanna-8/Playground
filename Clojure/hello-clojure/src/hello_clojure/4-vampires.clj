(ns hello-clojure.vampires
  (:gen-class))


(def filename "vampires.csv")


(defn split-csv
  [csvs]
  (clojure.string/split csvs #","))
;; ["Edward" "10"]


(defn mapify-values
  [values converters keys]
  (reduce conj {}
          (map (fn [val conv key] [key (conv val)])
               values converters keys)))
;; ["Edward Cullen" "10"] [identity read-string] [:name :age] ->
;; {name: "Edward Cullen", :age 10}


(defn read-suspects
  [filename]
  (->> (slurp filename)
       (clojure.string/split-lines)
       (map split-csv)))
;; (["Edward Cullen" "10"] ["Bella Swan" "0"] ...)


(defn mapify
  [filename converters keys]
  (map #(mapify-values % converters keys) (read-suspects filename)))
;; ({:name "Edward Cullen" :glitter 10} {:name "Bella Swan"  ...)

(mapify filename [identity read-string] [:name :glitter])


(defn glitter-filter
  [min-glitter suspects]
  (filter #(< min-glitter (:glitter %)) suspects))

(glitter-filter 3
 (mapify filename [identity read-string] [:name :glitter]))


(defn vampire-names
  [vampire-details]
  (map #(:name %) vampire-details))

(vampire-names
 (glitter-filter 3
  (mapify filename [identity read-string] [:name :glitter])))

(->> (mapify filename [identity read-string] [:name :glitter])
     (glitter-filter 3)
     (vampire-names))




