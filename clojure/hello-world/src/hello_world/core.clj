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

;it works!
(defn init-grid
  "Initializes a 2d array, with the specified size and initial value"
  [size, initial]
  (let [row (vec (repeat size initial))]
    (vec (repeat size row)))
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
  (def grid (init-grid 5 0.0))
  (def line (vec (repeat 5 1.0)))
  
  (println grid)
  (println line)

  (println (grid 0));also accesses the element at 0
  (println (line 0)); accesses the element at 0

  ;;both of these get you the element at 0,0
  (println ((grid 0)0))
  (println (get-in grid [0 0]))

  (assoc line 0 4)
  (println line)
  (println (assoc line 0 4))

  ;imagine the grid as a vector at the top from left to right,
  ; pointing to vectors beneath it whose indicies increase top to bottom

  ;(get grid index) returns the vector at index from the 2d grid 
  ; (println (get grid 0))
  ; (println (get (get grid 0) 0));this returns a single element @ 0,0
  ; (assoc grid 0 (assoc (get grid 0) 1 4.0));should set 0, 1 to 4, BUT DOESNT
  ; (println grid)

  ;;From: http://stackoverflow.com/a/8256512
  ;For maximum performance of numerical code in Clojure you will want to use:
  ;arrays, because you want mutable storage with very fast writes and lookup. 
  ;   Clojure sequences and vectors are beautiful, but they come with overheads
  ;   that you probably want to avoid for truly performance-critical code
  ;double primitives, because they offer much faster maths.
  ;aset / aget / areduce - these are extremely fast operations designed for 
  ;   arrays and basically give you the same bytecode as pure Java equivalents.
  ;imperative style - although it's unidiomatic in Clojure, it gets the 
  ;   fastest results (mainly because you can avoid overheads from memory
  ;   allocations, boxing and function calls). An example would be using 
  ;   dotimes for a fast imperative loop.
  ;(set! *warn-on-reflection* true) - and eliminate any warnings your code
  ;   produces, because reflection is a big performance killer.

  (def newArray (make-array Double/TYPE 5 5))
  (println newArray);prints it's address
  (println (aget newArray 0));print's a simgle array's address
  (println (aget newArray 0,0));print's a simgle element's value
  (aset newArray 0, 0, 5.0)
  (println (aget newArray 0,0));I think I've done it?


)


