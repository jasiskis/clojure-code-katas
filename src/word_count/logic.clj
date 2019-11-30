(ns word-count.logic
  (:require [clojure.string :as string]))

(defn rank [words-to-rank]
  (reduce (fn [acc word]
            (if (get acc word)
              (update acc word inc)
              (assoc acc word 1))) {} words-to-rank))

(defn normalize [words]
  (map #(-> %
            string/lower-case
            (string/replace #"[^a-z]" ""))
       words))

(defn split-text [text-to-split]
  (->> (string/split-lines text-to-split)
       (mapcat #(string/split % #" "))
       (filter (complement empty?))))
