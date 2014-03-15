;;I've imported java.io just so I have an example of importing more things
(ns hello-world.core
  (:gen-class)
  (:require [clojure.string :as cljstr])
  (:import (java.util Random)
    (java.io File)) 
)

;Compared to the Haskell hello World project I was working on, 
;  I'm actually going to copy a large amount of Clojure code straight from
;  the tutorial, as more of a personal reference.

;;;;;;;;;;;;;; records ;;;;;;;;;;;;;;;;;;;;
(defrecord Book [title author])
(defrecord PhoneContact [name, number])

;;;;;;;;;;;;;; functions ;;;;;;;;;;;;;;;;;;;;
;NOTE - function decls need to be before usage. 

;from tutorial?
(defn foo
  "I don't do a whole lot."
  [x]
  (println x ": Hello, World!"))

(defn init-grid
  "Initializes a 2d array, with the specified size and initial value"
  [size, initial]
  (def grid [])
  
  (loop [i 0]
    (if(< i size)
      (do;then
        ;;(println "arbitrary debug 0")
        (conj grid []);so this doesn't actually add a sub-element,
                      ; but it's a start
        (recur (inc i))
      )
      grid;else
    )
  )
  ;(grid)
)

;from tutorial
(defn square-corners 
  "Returns a 2D array (or list?) where the inner arrays are corners of a
    square starting at (top, left), with sides of dimension size"
  [top left size]
  (let [bottom (+ top size)
    right (+ left size)]
    [[top right],[top left],[bottom right][bottom left]])
)

;from tutorial
(defn ellipsize 
  "Takes the first three words from the parameter, and returns them followed 
    by an ellipse. Still works with less than 3 args, because it automatically
    converts the missing words to nils?, and println promptly ignores those"
  [words]
  (let [[w1 w2 w3] (cljstr/split words #"\s+")]
  (cljstr/join " " [w1 w2 w3 "..."]))
)

(defn -main
  ;This(below) is the "doc string", essentially built in javadoc.
  ;  which is pretty legit
  "This is the main for my really contrived clojure hello world project.
    Also, apparently, strings can go across multiple lines, which is a 
    little ridiculous, but handy"
  [& args]
  (println "Hello, World!")
  (foo "Mike")
  (println (.toUpperCase "lowercase"))
  (println 
    (apply str 
      (interleave "Attack at midnight" "The purple elephant chortled")
    )
  );I am probably a clojure heathen for putting my paren here.
  (def b (->Book "Return of the King" "JRRT"))
  (def bookDetails (str (:title b) " is by " (:author b)))
  (println bookDetails)

  (def deeds (->PhoneContact "Deidre" 4145555555))
  (def brohan (PhoneContact. "Dan" 2625555555))
  (println (:name deeds))
  (println (:number brohan))
  
  (println (square-corners 2 3 4))

  (println (ellipsize bookDetails))
  (println (ellipsize "onlyOneWordWhatHappensNow?"))
  ;Spoilers, prints: "onlyOneWordWhatHappensNow?   ..."
  (def mRand (new Random 0))
  (println (. mRand nextDouble))
  (println (. mRand nextDouble));@Haskell see now was that soo hard 

  (println [[],[]])
  (println (init-grid 5 0.0))
)


