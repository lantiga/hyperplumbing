(ns hyperplumbing.graph
  (:use plumbing.graph
        [exoref.delay :only [exodelay]])
  (:require [plumbing.lazymap :as lazymap]))


(defn distributed-compile
  [g]
  (abstract-compile
   g
   (fn [m] (into (lazymap/lazy-hash-map) m))
   (fn [m k f] (lazymap/delay-assoc m k (exodelay (name k) (f))))))

;; FIXME: right now the Redis key is set equal to k, which is not
;; correct. It should become the full keypath.
