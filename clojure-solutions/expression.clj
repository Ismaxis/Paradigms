(require 'clojure.math)

(defn division
  ([] 1)
  ([arg] (/ 1 (double arg)))
  ([first & args] (/ first (double (apply * args)))))

(def constant constantly)
(defn variable [name] (fn [map] (get map name)))
(defn operation [op] (fn [& args] (fn [map] (apply op (mapv #(% map) args)))))
(def add (operation +'))
(def subtract (operation -'))
(def multiply (operation *'))
(def divide (operation division))
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
(defn parseFunction [str] ((comp parseToken read-string) str))

; =============== HW-11 ===============

(definterface Expression
  (^Number eval [map])
  (^user.Expression diff [varName]))
(declare ZERO)
(deftype ConstantClass [val]
  Object
  (toString [this] (str (.-val this)))
  Expression
  (eval [this _] (.-val this))
  (diff [_ _] ZERO)
  )
(defn Constant [val] (ConstantClass. val))
(def ZERO (Constant 0))
(def ONE (Constant 1))
(def MINUS_ONE (Constant -1))
(deftype VariableClass [name]
  Object
  (toString [this] (.-name this))
  Expression
  (eval [this map] (get map (.-name this)))
  (diff [this varName] (if (= (.-name this) varName) ONE ZERO))
  )
(defn Variable [name] (VariableClass. name))
(declare Add)
(declare Multiply)
(deftype OperationClass [symbol operation operands getDiffCoefficient]
  Object
  (toString [this] (str "(" symbol " " (clojure.string/join " " (mapv #(.toString %) (.-operands this))) ")"))
  Expression
  (eval [this map] (apply operation (mapv #(.eval % map) (.-operands this))))
  (diff [this varName] (apply Add (map-indexed #(Multiply (apply getDiffCoefficient this %1 operands) (.diff %2 varName)) operands))
  ))
(defn Negate [& operands] (OperationClass. 'negate - operands (fn [& _] MINUS_ONE)))
(defn Add [& operands] (OperationClass. '+ + operands (fn [& _] ONE)))
(defn Subtract [& operands] (OperationClass. '- - operands
                                             (fn [_ i & operands]
                                               (if (zero? i) (if (== (count operands) 1) MINUS_ONE ONE) MINUS_ONE))))
(defn Multiply [& operands]
  (OperationClass. '* * operands
   (fn [_ i & operands] (apply Multiply (keep-indexed #(when ((comp not =) i %1) %2) operands)))))
(defn Divide [& operands]
  (OperationClass. '/ division operands
   (fn [this i numerator & denominators]
     (if (zero? (count denominators))
       (Negate (Divide this numerator))
      (if (== i 0)
      (apply Divide ONE denominators)
      (Negate (Divide this (nth denominators (- i 1))))
      ))
    )))

(defn evaluate [expression vars] (.eval expression vars))
(defn toString [expression] (.toString expression))
(defn diff [expression varName] (.diff expression varName))

;; TODO common parser
(def operationsObj { '+ Add, '- Subtract, '* Multiply, '/ Divide 'negate Negate }) ;, 'sumexp sumexp, 'lse lse})
(defn parseTokenObj [token] (cond
    (number? token) (Constant token)
     (symbol? token) (Variable (str token))
     (list? token) (apply (get operationsObj (first token)) (mapv parseTokenObj (rest token)))
    ))

(defn parseObject [str] ((comp parseTokenObj read-string) str))

; =============== TEST ZONE ===============
(def expr (diff (Divide (Variable "x")) "x"))
;(def expr (parseObject "(- (* 2 x) 3)"))

(println "abacaba")
(println (type expr))
(println (toString expr))
(println (evaluate expr {"z" 1.0, "x" 1.0, "y" 1.0}))
;(def diffed (diff expr "x"))
;(println diffed)
;(println (toString diffed))
;(println (evaluate diffed {"z" 2}))

;(def expr-str "(+ 1 (* x 2))")
;(def print-t (comp println type))
;(def parsed (parseFunction expr-str))
;;(def parsed (sumexp (variable "x")))
;(print-t parsed)
;(println (parsed {"z" 0.0, "x" 1.0, "y" 0.0}))
