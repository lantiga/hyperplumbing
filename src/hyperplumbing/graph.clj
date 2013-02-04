(ns hyperplumbing.graph
  (:use plumbing.graph
        [exoref.delay :only [exodelay]])
  (:require [lazymap.core :as lazymap]))


(defn distributed-compile
  ([g & groups]
     (let [g (plumbing.map/map-leaves-and-path
              (fn [keypath leaf] (vary-meta leaf assoc ::keypath keypath)) g)]
      (abstract-compile
       g
       (fn [m] (with-meta (into (lazymap/lazy-hash-map) m) (meta m)))
       (fn [m k f]
         (let [redis-key (or (:redis-key (meta f))
                             (clojure.string/join ":" (map name (::keypath (meta f)))))
               redis-key (if-let [redis-key-prefix (:redis-key-prefix (meta m))]
                           (str redis-key-prefix ":" redis-key)
                           redis-key)]
           (lazymap/delay-assoc
            m k
            (if (or (nil? groups) ((set groups) (:group (meta f))))
              (exodelay redis-key (restricted-call f m))
              (exodelay redis-key)))))))))
