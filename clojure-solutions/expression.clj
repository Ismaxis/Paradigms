(require 'clojure.math)
(require 'clojure.string)

(defn division
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
  (diff [this varName] (apply Add (map-indexed #(Multiply (apply getDiffCoefficient this %1 operands) (.diff %2 varName)) operands)))
  )
(defn Negate [& operands] (OperationClass. 'negate - operands (fn [& _] MINUS_ONE)))
(defn Add [& operands] (OperationClass. '+ + operands (fn [& _] ONE)))
(defn Subtract [& operands] (OperationClass. '- - operands
 (fn [_ i & operands] (cond
                        (== (count operands) 1) MINUS_ONE
                        (zero? i) ONE
                        :else MINUS_ONE))))
(defn Multiply [& operands] (OperationClass. '* * operands
  (fn [_ i & operands] (apply Multiply (keep-indexed #(when (not= i %1) %2) operands)))))
(defn Divide [& operands] (OperationClass. '/ division operands
  (fn [this i numerator & denominators]
   (if (zero? (count denominators))
     (Negate (Divide this numerator))
     (if (== i 0)
       (apply Divide ONE denominators)
       (Negate (Divide this (nth denominators (dec i))))
       ))
   )))
(defn square [x] (* x x))
(defn meansq-eval [& operands] (/ (apply + (mapv square operands)) (count operands)))
(defn Meansq [& operands] (OperationClass. 'meansq meansq-eval operands
  (fn [_ i & operands] (Divide (Multiply TWO (nth operands i)) (Constant (count operands))))))
(def rms-eval (comp clojure.math/sqrt meansq-eval))
(defn RMS [& operands] (OperationClass. 'rms rms-eval operands
  (fn [this i & operands] (Divide (nth operands i) (Multiply this (Constant (count operands)))))))
(def operations-obj { '+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate, 'meansq Meansq, 'rms RMS })
(def parseObject (parser Constant Variable operations-obj))
(defn evaluate [expression vars] (.eval expression vars))
(defn toString [expression] (.toString expression))
(defn diff [expression varName] (.diff expression varName))
