(ns word-count.main
  (:require [word-count.logic :as logic]
            [clojure.java.io :as io]))

(defn rank-text [text]
  (-> text
   logic/split-text
   logic/normalize
   logic/rank))

(def text (slurp (io/resource "word-count-fixture.txt")))

(defn -main []
  (println (take 10 (rank-text text))))
