(ns hyperplumbing.graph-test
  (:use clojure.test
        exoref.connection
        plumbing.core)
  (:require [plumbing.graph :as graph]
            [plumbing.fnk.pfnk :as pfnk]
            [hyperplumbing.graph :as hgraph]))

(def stats-graph
  "A graph specifying the same computation as 'stats' in the Graph sample"
  {:n  (fnk [xs]   (count xs))
   :m  (fnk [xs n] (/ (sum identity xs) n))
   :m2 (fnk [xs n] (/ (sum #(* % %) xs) n))
   :v  (fnk [m m2] (- m2 (* m m)))})

(deftest a-test
  (testing "Test distributed compile."
    (let [stats-eager (graph/eager-compile stats-graph)
          stats-distributed (with-conn (make-conn-pool) (make-conn-spec) (hgraph/distributed-compile stats-graph))
          arg {:xs [1 2 3]}
          stats-eager-val (stats-eager arg)
          stats-distributed-val (stats-distributed arg)]
      (is (= (:v stats-eager-val) (:v stats-distributed-val))))))
