# Solutions of Paradigms HW (M3136)

ITMO CT 2023-y1-spring

Course by [kgeorgiy](https://www.kgeorgiy.info/)

---

[Условия домашних заданий](https://www.kgeorgiy.info/courses/paradigms/homeworks.html)

## Домашнее задание 15. Разбор выражений на Prolog

Модификации
 * *Base*
    * Код должен находиться в файле `prolog-solutions/expression.pl`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/prtest/parsing/ParserTest.java)
        * Запускать c указанием модификации и типа (`polish` или `infix`).
 * *SinCos* (32, 34). Дополнительно реализовать поддержку:
    * унарных операций:
        * `op_sin` (`sin`) – синус, `sin(4846147)` примерно равно 1;
        * `op_cos` (`cos`) – косинус, `cos(5419351)` примерно равно 1.
 * *SinhCosh* (33, 35). Дополнительно реализовать поддержку:
    * унарных операций:
        * `op_sinh` (`sinh`) – гиперболический синус, `sinh(3)` немного больше 10;
        * `op_cosh` (`cosh`) – гиперболический косинус, `cosh(3)` немного меньше 10.
 * *Variables*. Дополнительно реализовать поддержку:
    * Переменных, состоящих из произвольного количества букв `XYZ` в любом регистре
        * Настоящее имя переменной определяется первой буквой ее имени
 * *VarBoolean* (36, 37). Сделать модификацию *Variables* и дополнительно реализовать поддержку:
    * Булевских операций
        * Аргументы: число больше 0 → `true`, иначе → `false`
        * Результат: `true` → 1, `false` → 0
        * `op_not` (`!`) - отрицание: `!5` равно 0
        * `op_and` (`&&`) – и: `5 & -6` равно 0
        * `op_or`  (`||`) - или: `5 & -6` равно 1
        * `op_xor` (`^^`) - исключающее или: `5 ^ -6` равно 1
 * *VarImplIff* (38, 39). Сделать модификацию *Boolean* и дополнительно реализовать поддержку:
    * Булевских операций
        * `op_impl` (`->`) – импликация (правоассоциативна): `-4 -> 1` равно 1
        * `op_iff`  (`<->`) - тогда и только тогда: `2 <-> 6` равно 1


## Домашнее задание 14. Дерево поиска на Prolog

Модификации
 * *Базовая*
    * Код должен находиться в файле `prolog-solutions/tree-map.pl`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/prtest/tree/TreeTest.java)
        * Запускать c указанием сложности (`easy` или `hard`) и модификации
 * *Keys* (32, 34)
    * Добавьте правило:
        * `map_keys(Map, Keys)`, возвращающее ключи в порядке возрастания.
 * *Value* (33, 35)
    * Добавьте правило:
        * `map_values(Map, Values)`, возвращающее значения в порядке возрастания ключей.
 * *PutIfAbsent* (36, 37)
    * Добавьте правило `map_putIfAbsent(Map, Key, Value, Result)`,
        добавляющее новый ключ и значение.
 * *PutCeiling* (38, 39)
    * Добавьте правила: 
        * `map_getCeiling(Map, Key, Value)`,
            возвращающее значение, соответствующее минимальному ключу, 
            большему либо равному заданному;
        * `map_putCeiling(Map, Key, Value, Result)`,
            заменяющее значение, соответствующее минимальному ключу, 
            большему либо равному заданному (если такой существует).


## Домашнее задание 13. Простые числа на Prolog

Модификации
 * *Базовая*
    * Код должен находиться в файле `prolog-solutions/primes.pl`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/prtest/primes/PrimesTest.java)
        * Запускать c указанием сложности (`easy`, `hard` или `bonus`) и модификации.
 * *Square* (32, 34)
    * Добавьте правило `square_divisors(N, D)`, возвращающее делители N²:
      `square_divisors(6, [2, 2, 3, 3])`.
 * *Cube* (33, 35)
    * Добавьте правило `cube_divisors(N, D)`, возвращающее делители N³:
      `cube_divisors(6, [2, 2, 2, 3, 3, 3])`.
 * *Compact* (36, 37)
    * Добавьте правило `compact_prime_divisors(N, CDs)`,
      где `CDs` — список пар (простое, степень):
        `compact_prime_divisors(120, [(2, 3), (3, 1), (5, 1)])`.
 * *Divisors* (38, 39)
    * Добавьте правило `divisors_divisors(N, Divisors)`,
      где `Divisors` — список разложений на простые делители делителей
      числа `N`:
        `divisors_divisors(12, [[], [2], [3], [2,2], [2,3], [2,2,3]])`.

Для запуска тестов можно использовать скрипты
[TestProlog.cmd](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/TestProlog.cmd) и [TestProlog.sh](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/TestProlog.sh)
 * Репозиторий должен быть скачан целиком.
 * Скрипты должны находиться в каталоге `prolog`
    (их нельзя перемещать, но можно вызывать из других каталогов).
 * Полное имя класса теста указывается в качестве первого аргумента командной строки,
    например, `prtest.primes.PrimesTest`.
 * Тестируемое решение должно находиться в текущем каталоге.


## Исходный код к лекциям по Prolog

Запуск Prolog
 * [Windows](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/RunProlog.cmd)
 * [*nix](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/RunProlog.sh)

Лекция 1. Факты, правила и вычисления
 * [Учебный план](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/1_1_plan.pl)
 * [Вычисления](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/1_2_calc.pl)
 * [Списки](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/1_3_lists.pl)
 * [Правила высшего порядка](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/1_4_high-order.pl)

Лекция 2. Задачи, унификация и объекты
 * [Задача о расстановке ферзей](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/2_1_queens.pl)
 * [Задача Эйнштейна](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/2_2_einstein.pl)
 * [Арифметические выражения](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/2_3_expressions.pl)

Лекция 3. Преобразование в строку и разбор
 * [Преобразование через термы](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/3_1_terms.pl)
 * [Преобразование через списки](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/3_2_chars.pl)
 * [Грамматики](https://www.kgeorgiy.info/git/geo/paradigms-2023/prolog/examples/3_3_grammar.pl)


## Домашнее задание 12. Комбинаторные парсеры

Модификации
 * *Base*
    * Код должен находиться в файле `clojure-solutions/expression.clj`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/cljtest/parsing/ParserTest.java)
        * Запускать c указанием модификации и сложности (`easy` или `hard`).
 * *Variables*. Дополнительно реализовать поддержку:
    * Переменных, состоящих из произвольного количества букв `XYZ` в любом регистре
        * Настоящее имя переменной определяется первой буквой ее имени
 * *IncDec* (32, 34). Сделать модификацию *Variables* и дополнительно реализовать поддержку:
    * Унарных операций:
        * `Inc` (`++`) – инкремент, `(33 ++)` равно 34;
        * `Dec` (`--`) – декремент, `(33 --)` равно 32.
 * *UPowLog* (33, 35). Сделать модификацию *Variables* и дополнительно реализовать поддержку:
    * Унарных операций:
        * `UPow` (`**`) – возведение в степень, `(8 **)` примерно равно 2981;
        * `ULog` (`//`) – натуральный логарифм абсолютной величины, `(2981 //)` примерно равно 8.
 * *Boolean* (36, 37). Сделать модификацию *Variables* и дополнительно реализовать поддержку:
    * Булевских операций
        * Аргументы: число больше 0 → `true`, иначе → `false`
        * Результат: `true` → 1, `false` → 0
        * `Not` (`!`) - отрицание: `!5` равно 0
        * `And` (`&&`) – и: `5 & -6` равно 0
        * `Or`  (`||`) - или: `5 & -6` равно 1
        * `Xor` (`^^`) - исключающее или: `5 ^ -6` равно 1
        * операции по увеличению приоритета: `^^`, `||`, `&&`, операции базовой модификации
 * *ImplIff* (38, 39). Сделать модификацию *Boolean* и дополнительно реализовать поддержку:
    * Булевских операций
        * `Impl` (`->`) – импликация (правоассоциативна): `-4 -> 1` равно 1
        * `Iff`  (`<->`) - тогда и только тогда: `2 <-> 6` равно 1
        * операции по увеличению приоритета: `<->`, `->`, операции модификации *Boolean*


## Домашнее задание 11. Объектные выражения на Clojure

Модификации
 * *Базовая*
    * Код должен находиться в файле `clojure-solutions/expression.clj`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/cljtest/object/ObjectTest.java)
        * Запускать c указанием модификации и сложности (`easy` или `hard`).
 * *SinCos* (32, 34). Дополнительно реализовать поддержку:
    * унарных операций:
        * `Sin` (`sin`) – синус, `(sin 4846147)` примерно равно 1;
        * `Cos` (`cos`) – косинус, `(cos 5419351)` примерно равно 1.
 * *SinhCosh* (33, 35). Дополнительно реализовать поддержку:
    * унарных операций:
        * `Sinh` (`sinh`) – гиперболический синус, `(sinh 3)` немного больше 10;
        * `Cosh` (`cosh`) – гиперболический косинус, `(cosh 3)` немного меньше 10.
 * *MeansqRMS* (36, 37). Дополнительно реализовать поддержку:
    * операций произвольного числа аргументов:
        * `Meansq` (`meansq`) – среднее квадратов, `(meansq 2 10 22)` равно 196;
        * `RMS` (`rms`) – [Root mean square](https://en.wikipedia.org/wiki/Root_mean_square), `(rms 2 10 22)` равно 14;
 * *SumexpLSE* (38, 39). Дополнительно реализовать поддержку:
    * операций произвольного числа аргументов:
        * `Sumexp` (`sumexp`) – сумма экспонент, `(sumexp 2 3 16)` примерно равно 8886137;
        * `LSE` (`lse`) – [LogSumExp](https://en.wikipedia.org/wiki/LogSumExp), `(lse 2 3 16)` примерно равно 16;


## Домашнее задание 10. Функциональные выражения на Clojure

Модификации
 * *Base*
    * Код должен находиться в файле `clojure-solutions/expression.clj`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/cljtest/functional/FunctionalTest.java)
        * Запускать c указанием модификации и сложности (`easy` или `hard`).
 * *ExpLn* (32, 34). Дополнительно реализовать поддержку:
    * унарных операций:
        * `exp` – экспонента, `(exp 8)` примерно равно 2981;
        * `ln`  – натуральный логарифм абсолютной величины, `(ln -2981)` примерно равно 8.
 * *ArcTan* (33, 35). Дополнительно реализовать поддержку:
    * операций:
        * `atan` – арктангенс, `(atan 1256)` примерно равно 1.57;
        * `atan2` – арктангенс, `(atan2 841 540)` примерно равно 1.
 * *SumexpLSE* (36, 37). Дополнительно реализовать поддержку:
    * операций произвольного числа аргументов:
        * `sumexp` – сумма экспонент, `(sumexp 2 3 16)` примерно равно 8886137;
        * `lse` – [LogSumExp](https://en.wikipedia.org/wiki/LogSumExp), `(lse 2 3 16)` примерно равно 16;
 * *MeansqRMS* (38, 39). Дополнительно реализовать поддержку:
    * операций произвольного числа аргументов:
        * `meansq` – среднее квадратов, `(meansq 2 10 22)` равно 196;
        * `rms` – [Root mean square](https://en.wikipedia.org/wiki/Root_mean_square), `(rms 2 10 22)` равно 14;


## Домашнее задание 9. Линейная алгебра на Clojure

Модификации
 * *Базовая*
    * Код должен находиться в файле `clojure-solutions/linear.clj`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/cljtest/linear/LinearTest.java)
        * Запускать c указанием сложности (`easy` или `hard`) и модификации.
 * *Shapeless* (32 - 35)
    * Добавьте операции поэлементного
        сложения (`s+`), вычитания (`s-`), умножения (`s*`) и деления (`sd`)
        чисел и векторов любой (в том числе, переменной) формы.
        Например, `(s+ [[1 2] 3] [[4 5] 6])`
        должно быть равно `[[5 7] 9]`.
 * *Tensor* (36, 37)
    * Назовем _тензором_ многомерную прямоугольную таблицу чисел.
    * Добавьте операции поэлементного
        сложения (`t+`), вычитания (`t-`), умножения (`t*`) и деления (`td`)
        тензоров.
        Например, `(t+ [[1 2] [3 4]] [[5 6] [7 8]])`
        должно быть равно `[[6 8] [10 12]]`.
 * *Broadcast* (38, 39)
    * Назовем _тензором_ многомерную прямоугольную таблицу чисел.
    * _Форма_ тензора – последовательность чисел
        (_s_<sub>1..n</sub>)=(_s_<sub>1</sub>, _s_<sub>2</sub>, …, _s<sub>n</sub>_), где
        _n_ – размерность тензора, а _s<sub>i</sub>_ – число элементов
        по _i_-ой оси.
      Например, форма тензора `[[[2 3 4] [5 6 7]]]`  равна (1, 2, 3),
      а форма `1` равна ().
    * Тензор формы (_s_<sub>1.._n_</sub>) может быть _распространен_ (broadcast)
      до тензора формы (_u_<sub>1.._m_</sub>), если (_s_<sub>i.._n_</sub>) является
      префиксом (_u<sub>1..m</sub>_).
      Для этого, элементы тензора копируются по недостающим осям.
      Например, распространив тензор `[[1 2]]` формы (1, 2) до
      формы (1, 2, 3) получим `[[[1 1 1] [2 2 2]]]`,
      а распространив `1` до формы (2, 3) получим `[[1 1 1] [1 1 1]]`.
    * Тензоры называются совместимыми, если один из них может быть распространен
      до формы другого.
      Например, тензоры формы (1, 2, 3) и (1, 2) совместимы, а
      (1, 2, 3) и (2, 1) – нет. Числа совместимы с тензорами любой формы.
    * Добавьте операции поэлементного
      сложения (`tb+`), вычитания (`tb-`), умножения (`tb*`) и деления (`tbd`)
      совместимых тензоров.
      Если формы тензоров не совпадают, то тензоры меньшей размерности
      должны быть предварительно распространены до тензоров большей размерности.
      Например, `(tb+ 1 [[10 20 30] [40 50 60]] [100 200])`
      должно быть равно `[[111 121 131] [241 251 261]]`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/cljtest/linear/BroadcastTester.java)


## Исходный код к лекциям по Clojure

Документация
 * [Clojure Reference](https://clojure.org/reference/documentation)
 * [Clojure Cheat Sheet](https://clojure.org/api/cheatsheet)

Запуск Clojure
 * Консоль: [Windows](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/RunClojure.cmd), [*nix](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/RunClojure.sh)
    * Интерактивный: `RunClojure`
    * С выражением: `RunClojure --eval "<выражение>"`
    * Скрипт: `RunClojure <файл скрипта>`
    * Справка: `RunClojure --help`
 * IDE
    * IntelliJ Idea: [плагин Cursive](https://cursive-ide.com/userguide/)
    * Eclipse: [плагин Counterclockwise](https://marketplace.eclipse.org/content/counterclockwise)

[Скрипт со всеми примерами](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples.clj)

Лекция 1. Функции
 * [Введение](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/1_1_intro.clj)
 * [Функции](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/1_2_functions.clj)
 * [Списки](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/1_3_lists.clj)
 * [Вектора](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/1_4_vectors.clj)
 * [Функции высшего порядка](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/1_5_functions-2.clj)

Лекция 2. Внешний мир
 * [Ввод-вывод](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/2_1_io.clj)
 * [Разбор и гомоиконность](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/2_2_read.clj)
 * [Порядки вычислений](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/2_3_evaluation-orders.clj)
 * [Потоки](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/2_4_streams.clj)
 * [Отображения и множества](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/2_5_maps.clj)

Лекция 3. Объекты
 * [Прототипное наследование](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/3_1_js-objects.clj)
    * Библиотека для ДЗ [proto.clj](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/proto.clj)
 * [Java-классы](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/3_2_java-objects.clj)
 * [Изменяемое состояние](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/3_3_mutable-state.clj)

Лекция 4. Комбинаторные парсеры
 * [Базовые функции](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/4_1_base.clj)
 * [Комбинаторы](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/4_2_combinators.clj)
    * Библиотека для ДЗ [parser.clj](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/parser.clj)
 * [JSON](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/4_3_json.clj)
 
Лекция 5. Макросы и основания математики
 * [Макросы](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/5_1_macro.clj)
 * [Парсеры](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/5_2_parser.clj)
 * [Числа Чёрча](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/examples/5_3_church.clj)


## Тестовое задание на Clojure

Это задание преднозначено для проверки правильности настройки Clojure.
Вам надо проверить, что оно успешно проверяется на вашем компьютере.

Для запуска тестов используются скрипты
[TestClojure.cmd](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/TestClojure.cmd) и [TestClojure.sh](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/TestClojure.sh)
 * Репозиторий должен быть скачан целиком.
 * Скрипты должны находиться в каталоге `clojure`
    (их нельзя перемещать, но можно вызывать из других каталогов).
 * Тестируемое решение должно находиться в текущем каталоге.
 * В качестве аргументов командной строки указывается 
   полное имя класса теста, сложность и модификация,
   например, `cljtest.example.ExampleTest hard base`.

Модификации
 * *base*
    * Код решения `clojure-solutions/example.clj`
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/clojure/cljtest/example/ExampleTest.java)
        * Запускать c аргументом `hard` или `easy`.


## Домашнее задание 8. Обработка ошибок на JavaScript

Модификации
 * *Base*
    * Код должен находиться в файле `javascript-solutions/objectExpression.js`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/jstest/prefix/ParserTest.java)
        * Запускать c указанием модификации и сложности (`easy` или `hard`).
 * *SumAvg* (32-35). Дополнительно реализовать поддержку:
    * операций произвольного числа аргументов:
        * `sum` – сумма, `(sum 1 2 3)` равно 6;
        * `avg` – среднее, `(avg 1 2 3)` равно 2;
 * *Postfix* (36-39). Дополнительно реализовать поддержку:
    * Выражений в постфиксной записи: 
        * `(2 3 +)` равно 5
        * функция `parsePostfix`
        * метод `postfix`
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/jstest/prefix/PostfixTest.java)
        * Запускать c указанием модификации и сложности (`easy` или `hard`).
 * *SumexpLSE* (36, 37). Дополнительно реализовать поддержку:
    * Операций произвольного числа аргументов:
        * `Sumexp` (`sumexp`) – сумма экспонент, `(2 3 16 sumexp)` примерно равно 8886137;
        * `LSE` (`lse`) – [LogSumExp](https://en.wikipedia.org/wiki/LogSumExp), `(2 3 16 lse)` примерно равно 16;
 * *MeansqRMS* (38, 39). Дополнительно реализовать поддержку:
    * операций произвольного числа аргументов:
        * `Meansq` (`meansq`) – среднее квадратов, `(2 10 22 meansq)` равно 196;
        * `RMS` (`rms`) – [Root mean square](https://en.wikipedia.org/wiki/Root_mean_square), `(2 10 22 rms)` равно 14;


## Домашнее задание 7. Объектные выражения на JavaScript

Модификации
 * *Base*
    * Код должен находиться в файле `javascript-solutions/objectExpression.js`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/jstest/object/ObjectTest.java)
        * Запускать c указанием модификации и сложности (`easy`, `hard` или `bonus`).
 * *ExpLn* (32, 34). Дополнительно реализовать поддержку:
    * унарных функций:
        * `Exp` (`exp`) – экспонента, `8 exp` примерно равно 2981;
        * `Ln`  (`Ln`)  – натуральный логарифм абсолютной величины,
            `2981 ln` примерно равно 8.
 * *ArcTan* (33, 35). Дополнительно реализовать поддержку:
    * функций:
        * `ArcTan` (`atan`) – арктангенс, `1256 atan` примерно равно 1.57;
        * `ArcTan2` (`atan2`) – арктангенс, `841 540 atan2` примерно равно 1.
 * *Distance* (36, 37). Дополнительно реализовать поддержку:
    * функций от `N` аргументов для `N=2..5`:
        * `SumSqN` (`sumsqN`) – сумма квадратов, `3 4 sumsq2` равно 25;
        * `DistanceN` (`distanceN`) – длина вектора, `3 4 distance2` равно 5.
 * *SumrecHMean* (38, 39). Дополнительно реализовать поддержку:
    * функций от `N` аргументов для `N=2..5`:
        * `SumrecN` (`sumrecN`) – сумма обратных величин, `1 2 3 6 sumrec4` равно 2;
        * `HMeanN` (`hmeanN`) – среднее гармоническое, `2 3 6 hmean3` равно 3.


## Домашнее задание 6. Функциональные выражения на JavaScript

Модификации
 * *Базовая*
    * Код должен находиться в файле `javascript-solutions/functionalExpression.js`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/jstest/functional/FunctionalTest.java)
        * Запускать c аргументом `hard` или `easy`.
 * *OneTwo* (32-39). Дополнительно реализовать поддержку:
    * констант:
        * `one` – 1;
        * `two` – 2;
 * *SinCos* (32, 34). Дополнительно реализовать поддержку:
    * операций:
        * `sin` – синус, `3.14159265 sin` примерно равно 0;
        * `cos` – косинус, `3.14159265 cos` примерно равно -1.
 * *SinhCosh* (33, 35). Дополнительно реализовать поддержку:
    * операций:
        * `sinh` – гиперболический синус, `(sinh 3)` немного больше 10;
        * `cosh` – гиперболический косинус, `(cosh 3)` немного меньше 10.
 * *FP* (36, 37). Дополнительно реализовать поддержку:
    * операций:
        * `*+` (`madd`) – тернарный оператор произведение-сумма, `2 3 4 *+` равно 10;
        * `_` (`floor`) – округление вниз `2.7 _` равно 2;
        * `^` (`ceil`) – округление вверх `2.7 ^` равно 3.
 * *ArgMinMax* (38, 39). Дополнительно реализовать поддержку:
    * операций:
        * `argMin3` – индекс минимального из трёх аргументов, `3 4 1 argMin3` равно 2;
        * `argMax3` – индекс максимального из трёх аргументов, `3 4 1 argMin3` равно 1;
        * `argMin5` – индекс минимального из пяти аргументов, `3 4 1 5 6 argMin3` равно 2;
        * `argMax5` – индекс максимального из пяти аргументов, `3 4 10 5 6 argMax3` равно 2;


## Исходный код к лекциям по JavaScript

[Скрипт с примерами](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples.js)

Запуск примеров
 * [В браузере](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/RunJS.html)
 * Из консоли
    * [на Java](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/RunJS.java): [RunJS.cmd](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/RunJS.cmd), [RunJS.sh](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/RunJS.sh)
    * [на node.js](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/RunJS.node.js): `node RunJS.node.js`

Лекция 1. Типы и функции
 * [Типы](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/1_1_types.js)
 * [Функции](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/1_2_functions.js)
 * [Функции высшего порядка](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/1_3_functions-hi.js).
   Обратите внимание на реализацию функции `mCurry`.
   Обратите внимание, что функции `array.map` и
   `array.reduce` (аналог `leftFold` входят в стандартную библиотеку).
 * [Пример: вектора и матрицы](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/1_4_vectors.js).

Лекция 2. Объекты и замыкания
 * [Поля](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/2_1_fields.js)
 * [Методы](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/2_2_methods.js)
 * [Замыкания](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/2_3_closures.js)
 * [Модули](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/2_4_modules.js)
 * [Пример: стеки](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/2_5_stacks.js)

Лекция 3. Другие возможности
 * [Обработка ошибок](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_1_errors.js)
 * [Чего нет в JS](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_2_no.js)
 * [Стандартная библиотека](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_3_builtins.js)
 * [Работа со свойствами](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_4_properties.js)
 * [Методы и классы](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_5_classes.js)
 * [JS 6+](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_6_js6.js)
 * Модули:
   [объявление](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_7_js6_module.mjs)
   [использование](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_7_js6_module_usage.mjs)
 * [Простейший ввод-вывод](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/examples/3_8_io.js)


## Тестовое задание на JavaScript

Это задание преднозначено для проверки правильности настройки JavaScript.
Вам надо проверить, что оно успешно проверяется на вашем компьютере.

Запуск тестов
 * Для запуска тестов используется [GraalJS](https://github.com/graalvm/graaljs)
   (часть проекта [GraalVM](https://www.graalvm.org/), вам не требуется их скачивать отдельно)
 * Для запуска тестов можно использовать скрипты [TestJS.cmd](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/TestJS.cmd) и [TestJS.sh](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/TestJS.sh)
    * Репозиторий должен быть скачан целиком.
    * Скрипты должны находиться в каталоге `javascript` (их нельзя перемещать, но можно вызывать из других каталогов).
    * В качестве аргументов командной строки указывается полное имя класса теста и модификация,
      например `jstest.example.ExampleTest hard base`.
 * Для самостоятельно запуска из консоли необходимо использовать командную строку вида:
    `java -ea --module-path=<js>/graal --class-path <js> jstest.functional.FunctionalTest {hard|easy} <variant>`, где
    * `-ea` – включение проверок времени исполнения;
    * `--module-path=<js>/graal` путь к модулям Graal (здесь и далее `<js>` путь к каталогу `javascript` этого репозитория);
    * `--class-path <js>` путь к откомпилированным тестам;
    * {`hard`|`easy`} указание тестируемой сложности;
    * `<variant>`} указание тестируемой модификации.
 * При запуске из IDE, обычно не требуется указывать `--class-path`, так как он формируется автоматически.
   Остальные опции все равно необходимо указать.
 * Troubleshooting
    * `Error occurred during initialization of boot layer java.lang.module.FindException: Module org.graalvm.truffle not found, required by jdk.internal.vm.compiler` – неверно указан `--module-path`;
    * `Graal.js not found` – неверно указаны `--module-path`
    * `Error: Could not find or load main class jstest.example.ExampleTest` – неверно указан `--class-path`;
    * `Exception in thread "main" java.lang.AssertionError: You should enable assertions by running 'java -ea jstest.functional.FunctionalExpressionTest'` – не указана опция `-ea`;
    * `Exception in thread "main" jstest.EngineException: Script 'example.js' not found` – в текущем каталоге отсутствует решение (`example.js`)


Модификации
 * *base*
    * Код решения `java-solutions/example.js`
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/javascript/jstest/example/ExampleTest.java)
        * Запускать c аргументом `hard` или `easy`.


## Домашнее задание 5. Вычисление в различных типах

Модификации
 * *Base*
    * Класс `GenericTabulator` должен реализовывать интерфейс
      [Tabulator](https://www.kgeorgiy.info/git/geo/paradigms-2023/java/expression/generic/Tabulator.java) и
      строить трехмерную таблицу значений заданного выражения.
        * `mode` – режим вычислений:
           * `i` – вычисления в `int` с проверкой на переполнение;
           * `d` – вычисления в `double` без проверки на переполнение;
           * `bi` – вычисления в `BigInteger`.
        * `expression` – выражение, для которого надо построить таблицу;
        * `x1`, `x2` – минимальное и максимальное значения переменной `x` (включительно)
        * `y1`, `y2`, `z1`, `z2` – аналогично для `y` и `z`.
        * Результат: элемент `result[i][j][k]` должен содержать
          значение выражения для `x = x1 + i`, `y = y1 + j`, `z = z1 + k`.
          Если значение не определено (например, по причине переполнения),
          то соответствующий элемент должен быть равен `null`.
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/java/expression/generic/GenericTest.java)
        * Первый аргумент: `easy` или `hard`
        * Последующие аргументы: модификации
 * *Ufs* (32-35)
    * Дополнительно реализуйте поддержку режимов:
        * `u` – вычисления в `int` без проверки на переполнение;
        * `f` – вычисления в `float` без проверки на переполнение;
        * `s` – вычисления в `short` без проверки на переполнение.
 * *Asm* (36-39)
    * Дополнительно реализуйте унарные операции:
        * `abs` – модуль числа, `abs -5` равно 5;
        * `square` – возведение в квадрат, `square 5` равно 25.
    * Дополнительно реализуйте бинарную операцию (максимальный приоритет):
        * `mod` – взятие по модулю, приоритет как у умножения (`1 + 5 mod 3` равно `1 + (5 mod 3)` равно `3`).
 * *Uls* (36, 37)
    * Дополнительно реализуйте поддержку режимов:
        * `u` – вычисления в `int` без проверки на переполнение;
        * `l` – вычисления в `long` без проверки на переполнение;
        * `s` – вычисления в `short` без проверки на переполнение.
 * *Ups* (38, 39)
    * Дополнительно реализуйте поддержку режимов:
        * `u` – вычисления в `int` без проверки на переполнение;
        * `p` – вычисления в целых числах по модулю 10079;
        * `s` – вычисления в `short` без проверки на переполнение.


## Домашнее задание 4. Очереди

Модификации
 * *Базовая*
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/java/queue/QueueTest.java)
    * [Откомпилированные тесты](artifacts/queue/QueueTest.jar)
 * *Count* (32, 34)
    * Реализовать метод `count`, возвращающий число вхождений элемента в очередь.
 * *Index* (33, 35)
    * Реализовать метод
        * `indexOf`, возвращающий индекс первого вхождения элемента в очередь;
        * `lastIndexOf`, возвращающий индекс последнего вхождения элемента в очередь.
    * Индексы отсчитываются с головы очереди.
    * Если искомого элемента нет, методы должны возвращать `-1`.
 * *Contains* (36, 37)
    * Добавить в интерфейс очереди и реализовать методы
        * `contains(element)` – проверяет, содержится ли элемент в очереди
        * `removeFirstOccurrence(element)` – удаляет первое вхождение элемента в очередь
            и возвращает было ли такое
    * Дублирования кода быть не должно
 * *Nth* (38, 39)
    * Добавить в интерфейс очереди и реализовать методы
        * `getNth(n)` – создать очередь, содержащую каждый n-й элемент, считая с 1
        * `removeNth(n)` – создать очередь, содержащую каждый n-й элемент, и удалить их из исходной очереди
        * `dropNth(n)` – удалить каждый n-й элемент из исходной очереди
    * Тип возвращаемой очереди должен соответствовать типу исходной очереди
    * Дублирования кода быть не должно


## Домашнее задание 3. Очередь на массиве

Модификации
 * *Базовая*
    * Классы должны находиться в пакете `queue`
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/java/queue/ArrayQueueTest.java)
    * [Откомпилированные тесты](artifacts/queue/ArrayQueueTest.jar)
 * *ToStr* (32, 34)
    * Реализовать метод `toStr`, возвращающий строковое представление
      очереди в виде '`[`' _голова_ '`, `' ... '`, `' _хвост_ '`]`'
 * *ToArray* (33, 35)
    * Реализовать метод `toArray`, возвращающий массив,
      содержащий элементы, лежащие в очереди в порядке
      от головы к хвосту.
 * *Deque*
    * Дополнительно реализовать методы
        * `push` – добавить элемент в начало очереди;
        * `peek` – вернуть последний элемент в очереди;
        * `remove` – вернуть и удалить последний элемент из очереди.
 * *DequeToArray* (36, 37)
    * Реализовать модификацию *Deque*;
    * Реализовать метод `toArray`, возвращающий массив,
      содержащий элементы, лежащие в очереди в порядке
      от головы к хвосту.
 * *DequeIndexed* (38, 39)
    * Реализовать модификацию *Deque*
    * Реализовать методы
        * `get` – получить элемент по индексу, отсчитываемому с головы;
        * `set` – заменить элемент по индексу, отсчитываемому с головы.
    * Для работы тестов необходимо добавить опцию JVM `--add-opens java.base/java.util=ALL-UNNAMED`


## Домашнее задание 2. Бинарный поиск

Модификации
 * *Базовая*
    * Класс `BinarySearch` должен находиться в пакете `search`
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/java/search/BinarySearchTest.java)
    * [Откомпилированные тесты](artifacts/search/BinarySearchTest.jar)
 * *Oddity* (32 - 37)
    * Если сумма всех чисел во входе чётная, то должна быть использоваться
      рекурсивная версия, иначе — итеративная.
 * *Shift* (32, 34)
    * На вход подается отсортированный (строго) по убыванию массив,
      циклически сдвинутый на `k` элементов. Требуется найти `k`.
      Все числа в массиве различны.
    * Класс должен иметь имя `BinarySearchShift`
 * *Max* (33, 35)
    * На вход подается циклический сдвиг
      отсортированного (строго) по возрастанию массива.
      Требуется найти в нём максимальное значение.
    * Класс должен иметь имя `BinarySearchMax`
 * *Uni* (36, 37)
    * На вход подается массив полученный приписыванием
      в конец массива отсортированного (строго) по возрастанию,
      массива отсортированного (строго) по убыванию.
      Требуется найти минимальную возможную длину первого массива.
    * Класс должен иметь имя `BinarySearchUni`
 * *Span* (38, 39)
    * На вход подаётся число `x` и массив, отсортированный по неубыванию.
      Требуется вывести два числа: начало и длину диапазона элементов, равных `x`.
      Если таких элементов нет, то следует вывести
      пустой диапазон, у которого левая граница совпадает с местом
      вставки элемента `x`.
    * Не допускается использование типов `long` и `BigInteger`.
    * Класс должен иметь имя `BinarySearchSpan`


## Домашнее задание 1. Обработка ошибок

Модификации
 * *Base*
    * Класс `ExpressionParser` должен реализовывать интерфейс
        [TripleParser](https://www.kgeorgiy.info/git/geo/paradigms-2023/java/expression/exceptions/TripleParser.java)
    * Классы `CheckedAdd`, `CheckedSubtract`, `CheckedMultiply`,
        `CheckedDivide` и `CheckedNegate` должны реализовывать интерфейс
        [TripleExpression](https://www.kgeorgiy.info/git/geo/paradigms-2023/java/expression/TripleExpression.java)
    * Нельзя использовать типы `long` и `double`
    * Нельзя использовать методы классов `Math` и `StrictMath`
    * [Исходный код тестов](https://www.kgeorgiy.info/git/geo/paradigms-2023/java/expression/exceptions/ExceptionsTest.java)
        * Первый аргумент: `easy` или `hard`
        * Последующие аргументы: модификации
 * *SetClear* (32-37)
    * Дополнительно реализуйте бинарные операции (минимальный приоритет):
        * `set` – установка бита, `2 set 3` равно 10;
        * `clear` – сброс бита, `10 clear 3` равно 2.
 * *Count* (32-37)
    * Дополнительно реализуйте унарную операцию
      `count` – число установленных битов, `count -5` равно 31.
 * *GcdLcm* (38, 39)
    * Дополнительно реализуйте бинарные операции (минимальный приоритет):
        * `gcd` – НОД, `2 gcd -3` равно 1;
        * `lcm` – НОК, `2 lcm -3` равно -6.
 * *Reverse* (38, 39)
    * Дополнительно реализуйте унарную операцию
      `reverse` – число с переставленными цифрами, `reverse -12345` равно `-54321`.
 * *PowLog10* (36-39)
    * Дополнительно реализуйте унарные операции:
        * `log10` – логарифм по уснованию 10, `log10 1000` равно 3;
        * `pow10` – 10 в степени, `pow10 4` равно 10000.

