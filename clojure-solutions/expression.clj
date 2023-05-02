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
(defn varName-to-key [name] (str (Character/toLowerCase^char (get name 0))))
(deftype VariableClass [name]
  Object
  (toString [_] (str name))
  Expression
  (eval [_ map] (get map (varName-to-key name)))
  (diff [_ varName] (if (= name varName) ONE ZERO))
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
(defn Not [& operands] (OperationClass. 'not #(not %) operands (constantly 'error) toStringInfix-unary))

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

; =============== HW-12 BOOLEAN ===============
(def to-bool (partial < 0))
(defn to-number [bool] (if bool 1 0))
(defn create-bool-op [op] (fn [a b] (to-number (op (to-bool a) (to-bool b)))))
(defn And [& operands] (OperationClass. '&& (create-bool-op #(and %1 %2)) operands
                                        (constantly 'error) toStringInfix-binary))
(defn Or [& operands] (OperationClass. '|| (create-bool-op #(or %1 %2)) operands
                                       (constantly 'error) toStringInfix-binary))
(defn Xor [& operands] (OperationClass. (symbol "^^") (create-bool-op #(not= %1 %2)) operands
                                        (constantly 'error) toStringInfix-binary))

(def operations-obj { '+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate,
                     'meansq Meansq, 'rms RMS,
                     '&& And, '|| Or, (symbol "^^") Xor, 'not Not })

(def parseObject (parser Constant Variable operations-obj))
(defn evaluate [expression vars] (.eval expression vars))
(defn toString [expression] (.toString expression))
(defn diff [expression varName] (.diff expression varName))

; ============================== HW-12 ==============================

(load-file "parser.clj")
(def +take-if _char)
(def *str-from-seq (comp +str +seq))
(defn *string [s] (apply *str-from-seq (mapv (comp +char str) (seq s))))
(def *space (+take-if #(Character/isWhitespace^char %)))
(def *ws ((comp +ignore +star) *space))
(def *skip-ws (partial +seqn 0 *ws))
(def *digit (+take-if #(Character/isDigit^char %)))
(def *decimal-point (*str-from-seq (+char ".") (+plus *digit)))
(def *unsigned (*str-from-seq ((comp +str +plus) *digit) (+opt *decimal-point)))
(def *number (+map read-string (*str-from-seq (+opt (+char "-")) *unsigned)))
(def *const (+map Constant (*skip-ws *number)))
(def *var-name ((comp +str +plus +char) "xyzXYZ"))
(def *var (+map Variable (*skip-ws *var-name)))
(declare *primitive)
(defn *parse-unary [symbol ctor] (+map ctor (*skip-ws (+seqn 1 (*string symbol) *ws (delay *primitive)))))
(def *not (*parse-unary "not" Not))
(def *negate (*parse-unary "negate" Negate))
(declare smallest-priority)
(def *brackets (delay (+seqn 1 (+char "(") *ws smallest-priority *ws (+char ")"))))
(def *primitive (+or *negate *not *const *var *brackets))
(def build-binary-tree (partial reduce (fn [sum [op val]] ((operations-obj ((comp symbol str) op)) sum val))))
(defn *parse-priority [symbols-parser next-priority]
  (+map build-binary-tree
        (+seqf cons next-priority (+star (+seq *ws symbols-parser *ws next-priority)))))
(def *mul-and-div (*parse-priority (+char "*/") *primitive))
(def *add-and-sub (*parse-priority (+char "+-") *mul-and-div))
(defn *parse-boolean [symbol next-priority] (*parse-priority (*string symbol) next-priority))
(def *and (*parse-boolean "&&" *add-and-sub))
(def *or (*parse-boolean "||" *and))
(def *xor (*parse-boolean "^^" *or))
(def smallest-priority (delay *xor))
(def parseObjectInfix (+parser (+seqn 0 *ws smallest-priority *ws)))
(defn toStringInfix [expression] (.toStringInfix expression))

; =============== TEST ZONE ===============

(println (operations-obj '&&))
(def expr-str "1&&0")
(def expr (parseObjectInfix expr-str))
(println "")
(println expr)
(println "")
(println (toString expr))
(println "")
(println (toStringInfix expr))
(println "")
(println (evaluate expr {"x" 0.0, "y" 2, "z" 1}))