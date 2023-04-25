(require 'clojure.math)

(defn division
  ([] 1)
  ([arg] (/ 1 (double arg)))
  ([first & args] (/ first (double (apply * args)))))
(defn parser [const-builder var-builder op-map]
  (letfn [(build-tree [token]
    (cond
      (number? token) (const-builder token)
      (symbol? token) (var-builder (str token))
      (list? token) (apply (get op-map (first token))
                           (mapv build-tree (rest token)))
    ))](comp build-tree read-string)
  ))

; =============== HW-10 ===============

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
(def operations-func { '+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'sumexp sumexp, 'lse lse })
(def parseFunction (parser constant variable operations-func))

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
  (toString [this] (str (.-name this)))
  Expression
  (eval [this map] (get map (.toString this)))
  (diff [this varName] (if (= (.toString this) varName) ONE ZERO))
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
(defn Multiply [& operands] (OperationClass. '* * operands
   (fn [_ i & operands] (apply Multiply (keep-indexed #(when ((comp not =) i %1) %2) operands)))))
(defn Divide [& operands] (OperationClass. '/ division operands
 (fn [this i numerator & denominators]
   (if (zero? (count denominators))
    (Negate (Divide this numerator))
    (if (== i 0)
      (apply Divide ONE denominators)
      (Negate (Divide this (nth denominators (- i 1))))
    ))
 )))
(def operations-obj { '+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate })
(def parseObject (parser Constant Variable operations-obj))
(defn evaluate [expression vars] (.eval expression vars))
(defn toString [expression] (.toString expression))
(defn diff [expression varName] (.diff expression varName))

; =============== TEST ZONE ===============

;(def expr (diff (Divide (Variable "x")) "x"))
;(def expr (parseObject "(- (* 2 x) 1)"))
;(println (toString expr))
;(println (evaluate expr {"z" 1.0, "x" 1.0, "y" 1.0}))
;(def diffed (diff expr "x"))
;(println diffed)
;(println (toString diffed))
;(println (evaluate diffed {"x" 2}))

;(def expr-str "(+ 1 (* x 2))")
;(def print-t (comp println type))
;(def parsed (parseFunction expr-str))
;;(def parsed (sumexp (variable "x")))
;(print-t parsed)
;(println (parsed {"z" 0.0, "x" 1.0, "y" 0.0}))
