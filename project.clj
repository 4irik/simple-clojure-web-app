(defproject patient "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [ring/ring-core "1.12.2"]
                 [ring/ring-jetty-adapter "1.12.2"]
                 [compojure "1.7.1"]
                 ]
  :repl-options {:init-ns patient.core}
  :profiles
  {:docker
   {:repl-options {:port 9911
		   :host "0.0.0.0"}
    :plugins [[cider/cider-nrepl "0.50.2"]]}}
  :main patient.core
  :aot [patient.core])
