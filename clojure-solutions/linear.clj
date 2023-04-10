(defn v+ [& args] (apply mapv + args))
(defn v- [& args] (apply mapv - args))
(defn v* [& args] (apply mapv * args))
(defn vd [& args] (apply mapv / args))

(defn v*s [v & s's] (mapv #(* % (apply * s's)) v))

(defn scalar [& args] (reduce + (apply v* args)))

(defn m+ [& args] (apply mapv v+ args))
(defn m- [& args] (apply mapv v- args))
(defn m* [& args] (apply mapv v* args))
(defn md [& args] (apply mapv vd args))

(defn m*s [m & s's] (mapv #(apply v*s % s's) m))
(defn m*v [m v] (mapv #(scalar v %) m))
(defn transpose [m] (apply mapv vector m))
(defn m*m [l r] (mapv #(m*v (transpose r) %) l))

(defn vect [a b] (let
    [ax (nth a 0) ay (nth a 1) az (nth a 2)
     bx (nth b 0) by (nth b 1) bz (nth b 2)]
  (vector (- (* ay bz) (* az by)) (- (* az bx) (* ax bz)) (- (* ax by) (* ay bx)))))