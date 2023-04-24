(require 'clojure.math)

(defn division [& args]
  (cond
    (== (count args) 0) 1
    (== (count args) 1) (/ 1 (double (first args)))
    :else (reduce #(/ %1 (double %2)) args)))

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
  (^Number eval [map]))

(deftype ConstantClass [val]
  Object
  (toString [this] (str (.-val this)))
  Expression
  (eval [this _] (.-val this))
  )
(defn Constant [val] (ConstantClass. val))
(deftype VariableClass [name]
  Object
  (toString [this] (.-name this))
  Expression
  (eval [this map] (get map (.-name this)))
  )
(defn Variable [name] (VariableClass. name))

(deftype OperationClass [symbol operation operands]
  Object
  (toString [this] (str "(" symbol " " (clojure.string/join " " (mapv #(.toString %) (.-operands this))) ")"))
  Expression
  (eval [this map] (apply operation (mapv #(.eval % map) (.-operands this))))
  )
(defn Add [& operands] (OperationClass. '+ + operands))
(defn Subtract [& operands] (OperationClass. '- - operands))
(defn Multiply [& operands] (OperationClass. '* * operands))
(defn Divide [& operands] (OperationClass. '/ division operands))
(defn Negate [& operands] (OperationClass. 'negate - operands))

(defn evaluate [expression vars] (.eval expression vars))
(defn toString [expression] (.toString expression))

(def operationsObj { '+ Add, '- Subtract, '* Multiply, '/ Divide 'negate Negate }) ;, 'sumexp sumexp, 'lse lse})
(defn parseTokenObj [token] (cond ;; TODO common parser
    (number? token) (Constant token)
     (symbol? token) (Variable (str token))
     (list? token) (apply (get operationsObj (first token)) (mapv parseTokenObj (rest token)))
    ))

(defn parseObject [str] ((comp parseTokenObj read-string) str))

; =============== TEST ZONE ===============
(def expr (Divide (Constant 5.0) (Variable "z")))
;(def expr (parseObject "(- (* 2 x) 3)"))
(println (type expr))
(println expr)
(println (toString expr))
(println (evaluate expr {"z" 0}))


;(def expr-str "(- (* 2 x) 3)")
;(def print-t (comp println type))
;;(def parsed (parseFunction expr-str))
;(def parsed (sumexp (variable "x")))
;(print-t parsed)
;(println (parsed {"z" 0.0, "x" 1.0, "y" 0.0}))
