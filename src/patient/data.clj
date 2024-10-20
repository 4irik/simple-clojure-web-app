(ns patient.data)

;; "Tables"
(def patients (atom []))

;; "Schema"
(def patient-keys [:fio :sex :date-of-birth :address :oms-number])

(defn get-patients
  "Возвращает список пациентов"
  []
  @patients)

(defn get-patient
  "Возвращает запись пациента по его ID"
  [id]
  (get @patients id))

(defn put-patient!
  "Добавляет новую запись"
  [patient]
  (swap! patients conj patient))

(defn del-patient!
  "Удаляет данные пациента"
  [id]
  (swap! patients #(vec (concat (subvec %1 0 %2) (subvec %1 (+ 1 %2)))) id))

(defn change-patient-one-value!
  "Изменят одно значение в записи пациента"
  [patient key new-value]
  (assoc patient key new-value))

(defn change-patient-values!
  "Изменяет значения в записи пациента"
  [patient new-values-map]
  (reduce
   #(let [key (%2 0) val (%2 1)] (change-patient-one-value! %1 key val))
   patient
   (seq new-values-map)))

(defn upd-patient!
  "Обновляет данные пациента (можно передавать только обновлённые поля)"
  [id new-data-of-patient]
  (def patient (get-patient id))
  (if (= nil patient)
    false
    (do
      (swap!
       patients
       #(vec
         (concat
          (subvec %1 0 %2)
          [(change-patient-values! %3 %4)]
          (subvec %1 (+ 1 %2))))
       id
       patient
       new-data-of-patient)
      true)))

