(ns word-count.word-count-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as m]
            [clojure.string :as string]
            [word-count.logic :as logic]))

(deftest simple-rank-test
  (testing "simple expectation"
    (let [words-to-rank ["a" "b" "b" "c"]
          expected {"b" 2 
                    "a" 1
                    "c" 1}]
      (is (match? expected (logic/rank words-to-rank)))))
  (testing "ordered map"
    (let [words-to-rank ["a" "c" "b" "b" "c" "c"]
          expected (sorted-map "c" 3 "b" 2 "a" 1)]
      (is (= expected (logic/rank words-to-rank))))))

(deftest simple-normalize-test
  (let [words-to-normalize ["THE" "the" "bla," "what?", "memory:"]
        expected ["the" "the" "bla" "what" "memory"]]
    (is (match? expected (logic/normalize words-to-normalize)))))

(deftest split-text-into-coll-test
  (let [text-to-split 
        "test test test
test test test      test
test test test
test."
        expected ["test" "test""test""test""test""test""test""test""test""test" "test."]]
    (is (match? expected (logic/split-text text-to-split)))))
