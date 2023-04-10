(defn binary-to-n [operation] #(reduce operation %&))

(defn vector-of-T? [T? v]
  (and (vector? v) (every? T? v)))

(def vector-of-numbers? (partial vector-of-T? number?))

(defn equal-size-vectors? [& args]
  (and (every? vector-of-numbers? args) (apply == (mapv count args))))

(defn v [operation]
  (fn [& args]
    {:pre [(apply equal-size-vectors? args)]}
    (apply mapv operation args)))

(def v+ (v +))
(def v- (v -))
(def v* (v *))
(def vd (v /))

(defn v*s [v & s's] (mapv #(* % (apply * s's)) v))

(defn scalar [& args] (reduce + (apply v* args)))

(defn matrix? [v]
  (vector-of-T? vector-of-numbers? v))

(defn equal-size-matrices? [args]
  (and (every? matrix? args)
       (apply == (mapv count args))                         ; all matrices have eq count of rows
       (every? #(apply == (mapv count %)) args)             ; all rows inside have eq size
       (apply == (mapv #(count (first %)) args))            ; all matrices have eq count of cols
       ))

(defn multiplied-matrices? [l r]
  (and (matrix? l) (matrix? r) (== (count (first l)) (count r))))

(defn m [operation]
  (fn [& args]
    {:pre [(equal-size-matrices? args)]}
    (apply mapv operation args)))

(def m+ (m v+))
(def m- (m v-))
(def m* (m v*))
(def md (m vd))


(defn m*s [m & s's]
  {:pre [(matrix? m) (every? number? s's)]}
  (mapv #(apply v*s % s's) m))
(defn m*v [m v] (mapv #(scalar v %) m))
(defn transpose [m] (apply mapv vector m))
(def m*m
  (binary-to-n
    (fn [l r]
      {:pre [(multiplied-matrices? l r)]}
      (mapv #(m*v (transpose r) %) l))))


(def vect
  (binary-to-n
    (fn [a b]
      {:pre [(vector-of-numbers? a) (vector-of-numbers? b) (== (count a) 3) (equal-size-vectors? a b)]}
      (let
        [ax (nth a 0) ay (nth a 1) az (nth a 2)
         bx (nth b 0) by (nth b 1) bz (nth b 2)]
        (vector (- (* ay bz) (* az by)) (- (* az bx) (* ax bz)) (- (* ax by) (* ay bx)))))))
