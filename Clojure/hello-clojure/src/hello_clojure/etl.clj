(ns hello-clojure.etl
  (:require [clojure.string :as str])
  (:require [clj-time.core :as time])
  (:require [clj-time.format :as tf]))


(def filename "humans.csv")


(defn extract
  [filename]
  (->> (slurp filename)
       (str/split-lines)
       (map #(str/split % #","))))


(defn mapify-age-part
  ;; ["4" "years"] => [:years 4]
  [[skey snr]]
  ([(keyword skey) (read-string snr)]))


(defn mapify-age
  ;; "1 years 3 days 5 months" => {years: 1, months: 5, days: 3}
  [age]
  (into {}
        (map (fn [sk sn] [(keyword sk) (read-string sn)])
             (filter not-empty (map str/trim (str/split age #"\d")))
             (map str/trim (str/split age #"[a-z]+")))))

(def datestr "1 days 1 years 1 months")
(mapify-age datestr)
(filter not-empty (map str/trim (str/split datestr #"\d")))
(map vector
     (filter not-empty (map str/trim (str/split datestr #"\d")))
     (map str/trim (str/split datestr #"[a-z]+")))

(defn birth-date
  ;; {years: 1, months: 1, days: 1} => Date one year & month & day ago.
  [age-map]
  (time/minus (time/now)
              (time/years (age-map :years))
              (time/months (age-map :months))
              (time/days (age-map :days))))

(birth-date {:years 1 :months 2 :days 1})
(birth-date (mapify-age "1 days, 1 years, 1 months"))


(defn transform-age
  ;; "1 days, 1 years, 1 months" => birthdate = today - 1y, 1m & 1d.
  [age]
  (let [age-map (mapify-age age)]
    (tf/unparse (tf/formatters :rfc822) (birth-date age-map))))

(transform-age "1 days, 1 years, 1 months")

(tf/unparse (tf/formatters :rfc822) (time/minus (time/now) (time/months 1) (time/days 1) (time/years 1)))
(tf/show-formatters)
(str (time/minus (time/now) (time/months 1) (time/days 1) (time/years 1)))


(defn transform
  [humans]
  (map (fn [[ln fn age]] [ln fn (transform-age age)])
       humans))

(defn jsonify-human
  [h]
  (str "\t{ \"name\": " (first h)
       ", \"surname\": " (second h)
       ", \"birthdate\": " (last h)
       " }")
  )

(defn jsonify
  [thumans]
  (str "{ \"humans\": [ \n"
       (str/join ",\n"
                            (map jsonify-human thumans))
       "\n\t]\n}"))

(defn etlfly [file] (jsonify (transform (extract file))))


;; spec!!

;; EVTL

