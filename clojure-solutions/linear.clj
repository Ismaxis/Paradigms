(defn vector-of-T? [T? v]
  (and (vector? v) (every? T? v)))

(def vector-of-numbers? (partial vector-of-T? number?))

(defn equal-size-vectors? [& args]
  (and (every? vector-of-numbers? args) (apply == (mapv count args))))

(defn applier [operation valid?]
  (fn [& args]
    {:pre [(apply valid? args)]
     :post [(valid? % (first args))]}
    (apply mapv operation args))
  )

(defn v [operation] (applier operation equal-size-vectors?))

(def v+ (v +))
(def v- (v -))
(def v* (v *))
(def vd (v /))

(defn v*s [v & scalars]
  {:pre [(vector-of-numbers? v) (every? number? scalars)]
   :post [(vector-of-numbers? %) (equal-size-vectors? % v)]}
   (mapv #(* % (apply * scalars)) v))

(defn scalar [& args]
  {:pre [(every? vector-of-numbers? args) (apply == (mapv count args))]
   :post [(number? %)]}
   (reduce + (apply v* args)))

(defn matrix? [v]
  (vector-of-T? vector-of-numbers? v))

(defn equal-size-matrices? [& args]
  (and (every? matrix? args)
       (apply == (mapv count args))                   ; all matrices have eq count of rows
       (every? #(apply == (mapv count %)) args)       ; all rows inside have eq size
       (apply == (mapv #(count (first %)) args))      ; all matrices have eq count of cols
       ))

(defn multiplied-matrices? [l r]
  (and (matrix? l) (matrix? r) (== (count (first l)) (count r))))

(defn m [operation] (applier operation equal-size-matrices?))

(def m+ (m v+))
(def m- (m v-))
(def m* (m v*))
(def md (m vd))

(defn m*s [m & s's]
  {:pre [(matrix? m) (every? number? s's)]
   :post [(matrix? %) (equal-size-matrices? m %)]}
  (mapv #(apply v*s % s's) m))

(defn m*v [m v]
  {:pre [(matrix? m) (vector-of-numbers? v)]
   :post [(vector-of-numbers? %) (== (count m) (count %))]}
  (mapv #(scalar v %) m))

(defn transpose [m]
  {:pre [(matrix? m)]
   :post [(matrix? %) (or (== (count %) 0) (and (multiplied-matrices? m %) (multiplied-matrices? % m)))]}
  (apply mapv vector m))

(defn binary-to-n [operation] #(reduce operation %&))
(def m*m
  (binary-to-n
    (fn [l r]
      {:pre [(multiplied-matrices? l r)]
       :post [(matrix? %) (== (count l) (count %)) (== (count (first r)) (count (first %)))]}
      (mapv #(m*v (transpose r) %) l))))


(def vect
  (binary-to-n
    (fn [l r]
      {:pre [(vector-of-numbers? l) (vector-of-numbers? r) (== (count l) 3) (equal-size-vectors? l r)]
       :post [(vector-of-numbers? %) (equal-size-vectors? % l)]}
      (let
        [lx (nth l 0) ly (nth l 1) lz (nth l 2)
         rx (nth r 0) ry (nth r 1) rz (nth r 2)]
        (vector (- (* ly rz) (* lz ry)) (- (* lz rx) (* lx rz)) (- (* lx ry) (* ly rx)))))))
