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

; ============================== HW-10 ==============================

(def constant constantly)
(defn variable [name] (fn [map] (get map name)))
(defn operation [op] (fn [& args] (fn [map] (apply op (mapv #(% map) args)))))
(def add (operation +'))
(def subtract (operation -'))
(def multiply (operation *'))
(def divide (operation division))

; =============== HW-10 SUMEXP-LSE ===============
(defn sum-of-exp [& args] (apply + (mapv clojure.math/exp args)))
(def sumexp (operation sum-of-exp))
(def lse (operation (fn [& args] (clojure.math/log (apply sum-of-exp args)))))
(def negate (operation -'))
(def operations-func { '+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'sumexp sumexp, 'lse lse })
(def parseFunction (parser constant variable operations-func))

; ============================== HW-11 ==============================

(definterface Expression
  (^Number eval [map])
  (^user.Expression diff [varName])
  (^String toStringInfix []))
(declare ZERO)
(deftype ConstantClass [val]
  Object
  (toString [this] (str (.-val this)))
  Expression
  (eval [this _] (.-val this))
  (diff [_ _] ZERO)
  (toStringInfix [this] (.toString this))
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
  (toStringInfix [this] (.toString this))
  )

(defn Variable [name] (VariableClass. name))
(declare Add)
(declare Multiply)
(deftype OperationClass [symbol operation operands getDiffCoefficient toStringInfix-nth]
  Object
  (toString [this] (str "(" symbol " " (clojure.string/join " " (mapv #(.toString %) (.-operands this))) ")"))
  Expression
  (eval [_ map] (apply operation (mapv #(.eval % map) operands)))
  (diff [this varName] (apply Add (map-indexed #(Multiply (apply getDiffCoefficient this %1 operands) (.diff %2 varName)) operands)))
  (toStringInfix [_]
    (toStringInfix-nth symbol operands)
  ))

(declare toStringInfix)
(defn toStringInfix-unary [symbol operands]
  (str symbol "(" (toStringInfix (nth operands 0)) ")"))
(defn Negate [& operands] (OperationClass. 'negate - operands (fn [& _] MINUS_ONE) toStringInfix-unary))

(defn toStringInfix-binary [symbol operands]
  (let [nth-operand (comp toStringInfix (partial nth operands))]
    (str "(" (nth-operand 0) " " symbol " " (nth-operand 1) ")")))
(defn Add [& operands] (OperationClass. '+ + operands (fn [& _] ONE) toStringInfix-binary))
(defn Subtract [& operands] (OperationClass. '- - operands
 (fn [_ i & operands] (cond (== (count operands) 1) MINUS_ONE (zero? i) ONE :else MINUS_ONE)) toStringInfix-binary))
(defn Multiply [& operands] (OperationClass. '* * operands
  (fn [_ i & operands] (apply Multiply (keep-indexed #(when (not= i %1) %2) operands))) toStringInfix-binary))
(defn Divide [& operands] (OperationClass. '/ division operands
  (fn [this i numerator & denominators]
   (if (zero? (count denominators))
     (Negate (Divide this numerator))
     (if (== i 0)
       (apply Divide ONE denominators)
       (Negate (Divide this (nth denominators (dec i)))))))
 toStringInfix-binary))

; =============== HW-11 MEANSQ-RMS ===============
(defn square [x] (* x x))
(defn meansq-eval [& operands] (/ (apply + (mapv square operands)) (count operands)))
(defn Meansq [& operands] (OperationClass. 'meansq meansq-eval operands
  (fn [_ i & operands] (Divide (Multiply TWO (nth operands i)) (Constant (count operands)))) toStringInfix-binary))
(def rms-eval (comp clojure.math/sqrt meansq-eval))
(defn RMS [& operands] (OperationClass. 'rms rms-eval operands
  (fn [this i & operands] (Divide (nth operands i) (Multiply this (Constant (count operands))))) toStringInfix-binary))

(def operations-obj { '+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate, 'meansq Meansq, 'rms RMS })
(def parseObject (parser Constant Variable operations-obj))
(defn evaluate [expression vars] (.eval expression vars))
(defn toString [expression] (.toString expression))
(defn diff [expression varName] (.diff expression varName))

; ============================== HW-12 ==============================

(load-file "parser.clj")
(def *take-if _char)
(def *str-from-seq (comp +str +seq))
(defn *string [s] (apply *str-from-seq (mapv (comp +char str) (seq s))))
(def *space (*take-if #(Character/isWhitespace %)))
(def *ws ((comp +ignore +star) *space))
(def *skip-ws (partial +seqn 0 *ws))
(def *digit (*take-if #(Character/isDigit %)))
(def *decimal-point (*str-from-seq (+char ".") (+plus *digit)))
(def *unsigned (*str-from-seq ((comp +str +plus) *digit) (+opt *decimal-point)))
(def *number (+map read-string (*str-from-seq (+opt (+char "-")) *unsigned)))
(def *const (+map Constant (*skip-ws *number)))
(def *identifier (+char "xyz"))
(def *var (+map Variable (*skip-ws *identifier)))
(declare *primitive)
(def *negate (+map Negate (*skip-ws (+seqn 1 (*string "negate") *ws (delay *primitive)))))
(declare smallest-priority)
(def *brackets (delay (+seqn 1 (+char "(") *ws smallest-priority *ws (+char ")"))))
(def *primitive (+or *negate *const *var *brackets))
(def build-binary-tree (partial reduce (fn [sum [op val]] ((operations-obj ((comp symbol str) op)) sum val))))
(defn *parse-priority [symbols-parser next-priority]
  (+map build-binary-tree
        (+seqf cons next-priority (+star (+seq *ws symbols-parser *ws next-priority)))))
(def *mul-and-div (*parse-priority (+char "*/") *primitive))
(def *add-and-sub (*parse-priority (+char "+-") *mul-and-div))
(def smallest-priority (delay *add-and-sub))
(def parseObjectInfix (+parser (+seqn 0 *ws smallest-priority *ws)))
(defn toStringInfix [expression] (.toStringInfix expression))

; =============== TEST ZONE ===============

;(def expr-str " (5.0  /   z)     ")
;(def expr (parseObjectInfix expr-str))
;(println "")
;(println expr)
;(println "")
;(println (toString expr))
;(println "")
;(println (toStringInfix expr))
;(println "")
;(println (evaluate expr {"x" 1, "y" 2, "z" 1}))