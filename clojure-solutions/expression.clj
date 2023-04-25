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
(def MINUS_ONE (Constant -1))
(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))
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
(def symbol-concat (comp symbol str))
(defn sumsq-eval [& operands] (apply + (mapv #(* % %) operands)))
(defn Sumsq [argsCount & operands] (OperationClass. (symbol-concat 'sumsq argsCount) sumsq-eval operands
  (fn [_ i & operands] (Multiply TWO (nth operands i)))
 ))
(def Sumsq2 (partial Sumsq 2))
(def Sumsq3 (partial Sumsq 3))
(def Sumsq4 (partial Sumsq 4))
(def Sumsq5 (partial Sumsq 5))
(defn distance-eval [& operands] (clojure.math/sqrt (apply sumsq-eval operands)))
(defn Distance [argsCount & operands] (OperationClass. (symbol-concat 'distance argsCount) distance-eval operands
  (fn [this i & operands] (Divide (nth operands i) this))
 ))
(def Distance2 (partial Distance 2))
(def Distance3 (partial Distance 3))
(def Distance4 (partial Distance 4))
(def Distance5 (partial Distance 5))
(def operations-obj { '+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate,
     'sumsq2 Sumsq2, 'sumsq3 Sumsq3, 'sumsq4 Sumsq4, 'sumsq5 Sumsq5,
     'distance2 Distance2, 'distance3 Distance3, 'distance4 Distance4, 'distance5 Distance5 })
(def parseObject (parser Constant Variable operations-obj))
(defn evaluate [expression vars] (.eval expression vars))
(defn toString [expression] (.toString expression))
(defn diff [expression varName] (.diff expression varName))

; =============== TEST ZONE ===============

;(def expr (Distance2 (Constant 2.0) (Constant 3.0)))
;(def expr (parseObject "(sumsq2 2.0 3.0)"))
;(println (toString expr))
;(println (evaluate expr {"z" 0.0, "x" 0.0, "y" 0.0}))
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
