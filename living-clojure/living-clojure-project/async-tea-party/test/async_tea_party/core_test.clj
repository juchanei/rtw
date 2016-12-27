(ns async-tea-party.core-test
  (:require [clojure.test :refer :all]
            [async-tea-party.core :refer :all]))

(deftest a-test
  (testing "TEST for TRUE"
    (request-tea)
    (is (= 1 1))))

(deftest b-test
  (testing "TEST for TRUE"
    (request-tea)
    (is (= 1 1))))


(deftest c-test
  (testing "TEST for TRUE"
    (request-tea)
    (is (= 1 1))))

(deftest d-test
  (testing "TEST for TRUE"
    (request-tea)
    (is (= 1 1))))

(deftest e-test
  (testing "TEST for TRUE"
    (request-tea)
    (is (= 1 1))))

(deftest f-test
  (testing "TEST for TRUE"
    (request-tea)
    (is (= 1 1))))
