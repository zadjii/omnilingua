(defproject hello-world "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]]
  ;;; Entry Point
  ;; The -main function in this namespace will be run at launch
  ;; (either via `lein run` or from an uberjar). It should be variadic:
  ;;
  ;; (ns my.service.runner
  ;;   (:gen-class))
  ;;
  ;; (defn -main
  ;;   "Application entry point"
  ;;   [& args]
  ;;   (comment Do app initialization here))
  ;;
  ;; This will be AOT compiled by default; to disable this, attach ^:skip-aot
  ;; metadata to the namespace symbol. ^:skip-aot will not disable AOT
  ;; compilation of :main when :aot is :all or contains the main class. It's
  ;; best to be explicit with the :aot key rather than relying on
  ;; auto-compilation of :main.
  :main hello-world.core

)


