(ns patient.core)

(require '[ring.adapter.jetty :refer [run-jetty]])
(require '[compojure.core :refer [GET POST PUT DELETE defroutes context]])

(defn make-response
  [response-string]
   {:status 200
   :headers {"content-type" "text/plain"}
   :body response-string})

(defn patient-list
  [request]
  (make-response "list of patients"))

(defn patient-view
  [request]
  (when-let [user-id (-> request :params :id)]
  (make-response (format "view patient #%s data" user-id))))

(defn patient-create
  [request]
  (make-response "new patient created"))

(defn patient-update
  [request]
  (make-response "patiend updated"))

(defn patient-delete
  [request]
  (make-response "patient deelted"))

(defn page-404
  [request]
  (make-response "No such a page."))

(defroutes app
  (GET "/"      request (patient-list request))
  (context "/patient" []
           (POST "/" request (patient-create request))
           (context "/:id{[0-9]+}" [id]
                    (GET "/" request (patient-view request))
                    (PUT "/" request (patient-update request))
                    (DELETE "/" request (patient-delete request))
                    )
           )
  page-404)


(defn -main
  [& args]
 (run-jetty app {:port 8080 :join? true}))
