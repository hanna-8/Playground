(ns hello-clojure.etl
  (:require [clojure.data.csv :as csv])
  (:require [clojure.data.json :as json])
  (:require [clojure.spec :as spec])
  (:require [clj-time.core :as time])
  (:require [clj-time.format :as tformat]))


;; E ...

(defn extract-age
  [age-str]
  (zipmap (map keyword (re-seq #"[a-z]+" age-str)) ;words
          (map read-string (re-seq #"\d+" age-str)))) ;numbers

(defn extract
  [csvs-blob]
  (->> (csv/read-csv csvs-blob)
       (map #(update-in % [2] extract-age))))


;; ... V ...

(def age-keys? #{:days :months :years})

(spec/def ::name (spec/and string? #(not (clojure.string/blank? %))))
(spec/def ::age (spec/map-of age-keys? number? :min-count 1))

(spec/def ::human (spec/tuple ::name ::name ::age))

(defn validate
  [humans]
  (filter #(spec/valid? ::human %) humans))


;; ... T ...

(defn birth-date
  [age]
  (time/minus (time/now)
              (time/years (age :years))
              (time/months (age :months))
              (time/days (age :days))))

(defn fill-age-gaps
  [age]
  (zipmap age-keys? (map #(or (age %) 0) age-keys?)))

(defn transform-age
  [age]
  (let [complete-age (fill-age-gaps age)]
  (tformat/unparse (tformat/formatters :rfc822) (birth-date complete-age))))

(defn transform
  [humans]
  (map #(update-in % [2] transform-age) humans))


;; ... L.

(defn load-human
  [[name surname birthdate]]
  (assoc {} :name name :surname surname :birthdate birthdate))

(defn load-to-json
  [humans]
  (json/write-str {:humans (map load-human humans)}))
X

;; Run.

(def in-file "humans.csv")
(def out-file "humans.json")

(defn csv-to-json
  [inf outf]
  (->> (slurp inf) ; side-effect
       extract
       validate
       transform
       load-to-json
       (spit outf))) ; side-effect

