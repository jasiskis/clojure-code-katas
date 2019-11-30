(ns partition-by.partition-by-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test]))

;; Kata:
;;  - Implement clojure's partition-by
;;  - Make sure it is lazy

;; TODO: Still not working as expected
(defn my-partition-by [f coll]
  (loop [[first' & rest' :as coll] coll
         partitioned []
         open-partition []
         last' nil]
      (cond
        (empty? coll) (conj partitioned open-partition)
        (not= (f first') (some-> last' f)) (recur rest' (conj partitioned (conj open-partition first')) [first'] first')
        :else
        (recur rest' partitioned (conj open-partition first') first'))))

(my-partition-by odd? [1 1 3 3 5 4 5 5])

(deftest partition-all-test
  (testing "simple partitioning should split every one element"
    (is (match? (my-partition-by odd? (range 10))
                (partition-by odd? (range 10))))))
