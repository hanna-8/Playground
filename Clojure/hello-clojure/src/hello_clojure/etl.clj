(ns hello-clojure.etl
  (:require [clojure.string :as str])
  (:require [clj-time.core :as time])
  (:require [clj-time.format :as tf])
  (:require [clojure.data.csv :as csv])
  (:require [clojure.data.json :as json]))


(defn extract-age
  [age-str]
  (zipmap (map keyword (re-seq #"[a-z]+" age-str))
          (map read-string (re-seq #"\d" age-str))))

; (extract-age " 1 years,   5 days, 3 months")
; {:years 1, :days 5, :months 3}


(defn extract
  [csvs-blob]
  (->> (csv/read-csv csvs-blob)
       (map (fn [[n s a]] [n s (extract-age a)]))))

;; (extract in-file)
;; (["Edward" "Cullen" {:years 1, :months 0, :days 9}] ...


(defn valid-name
  [name]
  (some? (re-matches #"[A-Za-z \\-]+" name)))

(defn valid-age
  [age]
  (or (some? (:years age))
      (some? (:months age))
      (some? (:days age))))

(defn validate
  [humans]
  (filter (fn 
            [[name surname age]]
            (and (valid-name name)
                 (valid-name surname)
                 (valid-age age)))
          humans))


(defn fill-nil
  [val]
  (if (nil? val) 0 val))

(defn birth-date
  ;; {years: 1, months: 1, days: 1} => Date one year & month & day ago.
  [age]
  (time/minus (time/now)
              (time/years (fill-nil (age :years)))
              (time/months (fill-nil (age :months)))
              (time/days (fill-nil (age :days)))))

(birth-date {:years 1 :months 2 :days 1})
(birth-date {:days 1})
;; (birth-date (mapify-age "1 days, 1 years, 1 months"))

(defn transform-age
  ;; "1 days, 1 years, 1 months" => birthdate = today - 1y, 1m & 1d.
  [age]
  (tf/unparse (tf/formatters :rfc822) (birth-date age)))

;; (transform-age "1 days, 1 years, 1 months")


(defn transform
  [humans]
  (map (fn [[ln fn age]] [ln fn (transform-age age)]) humans))


(defn load-human
  [[name surname birthdate]]
  (json/write-str {:name name :surname surname :birthdate birthdate}))


(defn load-json
  [humans]
  (json/write-str {:humans (map load-human humans)}))


(def in-file "humans.csv")
(def out-file "humans.json")

(defn csv-to-json
  [in-file]
  (->> (slurp in-file)
       extract
       validate
       transform
       load-json
       (spit out-file)))



