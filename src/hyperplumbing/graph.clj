(ns hyperplumbing.graph
  (:use plumbing.graph
        [exoref.delay :only [exodelay]])
  (:require [lazymap.core :as lazymap]))


(defn distributed-compile
  ([g & groups]
     (abstract-compile
      g
      (fn [m] (with-meta (into (lazymap/lazy-hash-map) m) (meta m)))
      (fn [m k f]
        (let [;; in the following two lines we need k to be the key path, not just the key
              ;; TODO: modify abstract-compile in plumbing.graph
              fnk-meta (meta (get-in g [k]))
              redis-key (name (or (:redis-key fnk-meta) k))
              redis-key (if-let [redis-key-prefix (:redis-key-prefix (meta m))]
                          (str redis-key-prefix ":" redis-key)
                          redis-key)]
          (lazymap/delay-assoc
           m k
           (if (or (nil? groups) ((set groups) (:group fnk-meta)))
             (exodelay redis-key (f))
             (exodelay redis-key))))))))
