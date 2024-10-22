(ns patient.core)

(require '[ring.adapter.jetty :refer [run-jetty]])
(require '[compojure.core :refer [GET POST PATCH DELETE defroutes context wrap-routes]])
(require '[compojure.coercions :refer [as-int]])
(require '[patient.data :as db])и

(defn make-response
  [response-string]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body response-string})

;; todo: должен быть 404 код ответа
(defn page-404
  [request]
  (make-response "No such a page."))

(defn patient-list
  [request]
  (make-response (str (db/get-patients))))

(defn patient-view
  [id]
  (let [patient (db/get-patient id)]
    (if patient
      (make-response (str patient))
      (page-404 []))))

(defn patient-create
  [patient-data]
  (db/put-patient! patient-data)
  (make-response nil))

(defn patient-update
  [id patient-data]
  (if (db/upd-patient! id patient-data)
    (make-response nil)
    (page-404 [])))

(defn patient-delete
  [id]
  (if (db/get-patient id)
    (do
      (db/del-patient! id)
      (make-response nil))
    (page-404 [])))

(defn wrap-patient-data
  [handler]
  (fn
    [request]
    (let
        [patient-raw (slurp (:body request))
         patient-map (clojure.edn/read-string (str "{" patient-raw "}"))]
      (handler (assoc request :patient-data patient-map)))))

(defroutes app
  (GET "/" request (patient-list request))
  (context "/patient" []
           (->
            (POST "/" {:keys [patient-data]} (patient-create patient-data))
            (wrap-routes wrap-patient-data))
           (context "/:id{[0-9]+}" [id :<< as-int]
                    (GET "/" [] (patient-view id))
                    (->
                     (PATCH "/" {:keys [patient-data]} (patient-update id patient-data))
                     (wrap-routes wrap-patient-data))
                    (DELETE "/" [] (patient-delete id))))
  page-404)


(defn -main
  [& args]
 (run-jetty app {:port 8080 :join? true}))
