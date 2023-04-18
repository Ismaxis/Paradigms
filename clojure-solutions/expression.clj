(require 'clojure.math)
(defn constant [val] (fn [& _] val))
(defn variable [name] (fn [map] (get map name)))
(defn operation [op] (fn [& args] (fn [map] (apply op (mapv #(% map) args)))))
(def add (operation +'))
(def subtract (operation -'))
(def multiply (operation *'))
(def divide (operation (fn [& args]
                (if (== (count args) 1) (/ 1 (double (first args)))
                                        (reduce #(/ %1 (double %2)) args)))))

(defn sum-of-exp [& args] (apply + (mapv clojure.math/exp args)))
(def sumexp (operation sum-of-exp))
(def lse (operation (fn [& args] (clojure.math/log (apply sum-of-exp args)))))
(def negate (operation -'))
(def operations {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'sumexp sumexp, 'lse lse})
(defn parseToken [token] (cond
    (number? token) (constant token)
    (symbol? token) (variable (str token))
    (list? token) (apply (get operations (first token)) (mapv parseToken (rest token)))
    ))
(defn parseFunction [str] (parseToken (read-string str)))

; =============== TEST ZONE ===============

;(def expr-str "(- (* 2 x) 3)")
;(def print-t (comp println type))
;;;(def parsed (parseFunction expr-str))
;(def parsed (sumexp (variable "x")))
;(print-t parsed)
;(println (parsed {"z" 0.0, "x" 1.0, "y" 0.0}))
