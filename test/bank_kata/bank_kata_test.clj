(ns bank-kata.bank-kata-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test]
            [clojure.string :as string])
  (:import [java.time LocalDate Month]))

(defn deposit [amount current-balance]
  {:balance (+ amount (:balance current-balance))})

(defn withdrawal [amount current-balance]
  {:balance (- (:balance current-balance) amount)})

(defmulti pretty-print-movement :type)
(defmethod pretty-print-movement :deposit [movement]
  (str (:date movement) "," (:amount movement) "," "," (:balance movement)))

(defmethod pretty-print-movement :withdrawal [movement]
  (str (:date movement) "," "," (:amount movement) "," (:balance movement)))

(defn convert-date [date-as-string]
  (let [[day month year] (string/split date-as-string #"/")]
    (LocalDate/of (Integer/parseInt year)
                  (Month/of (-> month Integer/parseInt)) 
                  (Integer/parseInt day))))

(defn assoc-localdate [m] (assoc m :local-date (convert-date (:date m))))

(defn statement [movements]
  (let [header "Date,Credit,Debit,Balance"]
    (->> movements
         (map assoc-localdate)
         (sort-by :local-date)
         reverse
         (map pretty-print-movement)
         (concat [header])
         (string/join \newline))))

(deftest deposit-test
  (testing "simple deposit"
    (is (match? {:balance 1000}
                (deposit 1000 {:balance 0})))
    (is (match? {:balance 2000}
                (deposit 1000 {:balance 1000})))))

(deftest withdrawal-test
  (testing "simple withdrawal"
    (is (match? {:balance 0}
                (withdrawal 1000 {:balance 1000})))
    (is (match? {:balance 1000}
                (withdrawal 2000 {:balance 3000})))))

(deftest statement-test
  (testing "statement report"
    (is (match?
         "Date,Credit,Debit,Balance
14/01/2012,,500,2500"
         (statement [{:date "14/01/2012"
                      :type :withdrawal
                      :amount 500
                      :balance 2500}])))
    (is (match?
         "Date,Credit,Debit,Balance
14/01/2012,,500,2500
13/01/2012,2000,,3000"
         (statement [{:date "14/01/2012"
                      :type :withdrawal
                      :amount 500
                      :balance 2500}
                     {:date "13/01/2012"
                      :type :deposit
                      :amount 2000
                      :balance 3000}])))
   (testing "out of order movements"
     (is (match?
          "Date,Credit,Debit,Balance
14/01/2012,,500,2500
13/01/2012,2000,,3000
10/01/2012,1000,,1000"
          (statement [{:date "14/01/2012"
                       :type :withdrawal
                       :amount 500
                       :balance 2500}
                      {:date "10/01/2012"
                       :type :deposit
                       :amount 1000
                       :balance 1000}
                      {:date "13/01/2012"
                       :type :deposit
                       :amount 2000
                       :balance 3000}]))))))


(deftest date-conversion-test
  (testing "date conversion"
    (is (match? (LocalDate/of 2012 Month/JANUARY 14)
                (convert-date "14/01/2012")))
    (is (match? (LocalDate/of 2012 Month/JANUARY 13)
                (convert-date "13/01/2012")))
    (is (match? (LocalDate/of 2012 Month/FEBRUARY 13)
                (convert-date "13/02/2012")))))


;; controller 
;; TODO: Implement outer logic

(defn controller-deposit! [state amount])

(deftest controller-test 
  (testing "new account"
    (let [state (atom [])]
      (controller-deposit! state 1000)
      (is (match? [{:type :deposit
                    :amount 1000
                    :balance 1000}] @state)))))
