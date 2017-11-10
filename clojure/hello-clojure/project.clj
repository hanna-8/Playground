(defproject hello-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [ [org.clojure/clojure "1.9.0-alpha15"]
                  [clj-time "0.13.0"]
                  [org.clojure/data.csv "0.1.3"]
                  [org.clojure/test.check "0.9.0"]
                  [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot hello-clojure.core
  :target-path "target/%s"
  :plugins [ [lein-ancient "0.6.10"] ]
  :profiles {:uberjar {:aot :all}})
