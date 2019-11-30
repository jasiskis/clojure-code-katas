(ns kata02.chop-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test]))

(defn chop 
  [n array]
   (let [size (count array)
         half (int (/ size 2))
         current (nth array half nil)
         special-sum (fn [x y] (if (pos? x) (+ x y) -1))]
     (cond
       (nil? current) -1
       (= current n) half
       (= 1 size) -1
       (> current n) (chop n (->> array (split-at half) first))
       (< current n) (special-sum (chop n (->> array (split-at half) second)) half)
       :else -1)))

(deftest chop-test
  (testing "sample cases"

    (is (match? -1 (chop 3 [])))
    (is (match? -1 (chop 3 [1])))
    (is (match? 0  (chop 1 [1])))
    (is (match? 0  (chop 1 [1 3 5])))
    (is (match? 1  (chop 3 [1 3 5])))
    (is (match? 2  (chop 5 [1 3 5])))
    (is (match? -1 (chop 0 [1 3 5])))
    (is (match? -1 (chop 2 [1 3 5])))
    (is (match? -1 (chop 4 [1 3 5])))
    (is (match? -1 (chop 6 [1 3 5])))

    (is (match? 0 (chop 1 [1 3 5 7])))
    (is (match? 1 (chop 3 [1 3 5 7])))
    (is (match? 2 (chop 5 [1 3 5 7])))
    (is (match? 3 (chop 7 [1 3 5 7])))
    (is (match? -1 (chop 0 [1 3 5 7])))
    (is (match? -1 (chop 2 [1 3 5 7])))
    (is (match? -1 (chop 4 [1 3 5 7])))
    (is (match? -1 (chop 6 [1 3 5 7])))
    (is (match? -1 (chop 8 [1 3 5 7])))))
