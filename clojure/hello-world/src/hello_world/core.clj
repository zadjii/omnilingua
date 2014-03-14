(ns hello-world.core
    (:gen-class))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x ": Hello, World!"))

(defn -main
  "Application entry point"
  [& args]
  (println "Hello, World!")
  (foo "Mike")
)
