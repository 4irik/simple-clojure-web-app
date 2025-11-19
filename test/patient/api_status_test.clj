(ns patient.api-status-test
  (:require [clojure.test :refer :all]
            [patient.core :refer :all]
            [patient.data :as db]))

(defn reset-db-fixture [f]
  (reset! db/patients [])
  (f))

(use-fixtures :each reset-db-fixture)

(deftest test-status-codes
  (testing "patient-list returns 200"
    (is (= 200 (:status (patient-list {})))))

  (testing "patient-create returns 201"
    (is (= 201 (:status (patient-create {:fio "Test" :sex "M"})))))

  (testing "patient-view returns 200 for existing patient"
    (db/put-patient! {:fio "Test" :sex "M"})
    (is (= 200 (:status (patient-view 0)))))

  (testing "patient-view returns 404 for non-existing patient"
    (is (= 404 (:status (patient-view 999)))))

  (testing "patient-update returns 204 for existing patient"
    (db/put-patient! {:fio "Test" :sex "M"})
    (is (= 204 (:status (patient-update 0 {:fio "Updated"})))))

  (testing "patient-update returns 404 for non-existing patient"
    (is (= 404 (:status (patient-update 999 {:fio "Updated"})))))

  (testing "patient-delete returns 204 for existing patient"
    (db/put-patient! {:fio "Test" :sex "M"})
    (is (= 204 (:status (patient-delete 0)))))

  (testing "patient-delete returns 404 for non-existing patient"
    (is (= 404 (:status (patient-delete 999))))))
