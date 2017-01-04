(ns daily-training.core)

"1주차 - 1일차"

(defn Nothing-but-the-Truth []
  (= true true))

(defn Simple-Math []
  (= (- 10 (* 2 3)) 4))

(defn Intro-to-Strings []
  (= "HELLO WORLD" (.toUpperCase "hello world")))

(defn Intro-to-Lists []
  (= (list :a :b :c) '(:a :b :c)))

(defn Lists-conj []
  ((= '(1 2 3 4) (conj '(2 3 4) 1))
  (= '(1 2 3 4) (conj '(3 4) 2 1))))

(defn Intro-to-Vectors []
  (= [:a :b :c] (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c)))

(defn Intro-to-Sets []
  ((= #{:a :b :c :d} (set '(:a :a :b :c :c :c :c :d :d)))
  (= #{:a :b :c :d} (clojure.set/union #{:a :b :c} #{:b :c :d}))))

(defn Sets-conj []
  ((= #{1 2 3 4} (conj #{1 4 3} 1))))

(defn Intro-to-Maps []
  ((= 20 ((hash-map :a 10, :b 20, :c 30) :b))
  (= 20 (:b {:a 10, :b 20, :c 30}))))

(defn Maps-conj []
  ((= {:a 1, :b 2, :c 3} (conj {:a 1} {:b 2} [:c 3]))))

(defn Intro-to-Sequences []
  ((= 3 (first '(3 2 1)))
  (= 3 (second [2 3 4]))
  (= 3 (last (list 1 2 3)))))

"1주차 - 2일차"

(defn Sequences-rest []
  (= [20 30 40] (rest [10 20 30 40])))

(defn Intro-to-Functions []
  ((= 8 ((fn add-five [x] (+ x 5)) 3))
  (= 8 ((fn [x] (+ x 5)) 3))
  (= 8 (#(+ % 5) 3))
  (= 8 ((partial + 5) 3))))

(defn Double-Down []
  ((= ((fn dobule[x] (* x 2)) 2) 4)
  (= ((fn [x] (* x 2)) 3) 6)
  (= (#(* % 2) 11) 22)
  (= ((partial * 2) 7) 14)))

(defn Hello-World []
  ((= (#(str "Hello, " % "!") "Dave") "Hello, Dave!")
  (= ((fn hello [the-name] (str "Hello, " the-name "!")) "Jenn") "Hello, Jenn!")
  (= ((fn [the-name] (str "Hello, " the-name "!")) "Rhea") "Hello, Rhea!")))

(defn Sequences-map []
  (= '(6 7 8)' (map #(+ % 5) '(1 2 3))))

(defn Sequences-filter []
  (= __ (filter #(> % 5) '(3 4 5 6 7))))

(defn Local-bindings []
  ((= 7 (let [x 5] (+ 2 x)))
  (= 7 (let [x 3, y 10] (- y x)))
  (= 7 (let [x 21] (let [y 3] (/ x y))))))

(defn Let-it-Be []
  ((= 10 (let [x 7 y 3 z 1] (+ x y)))
  (= 4 (let [x 7 y 3 z 1] (+ y z)))
  (= 1 (let [x 7 y 3 z 1] z))))

"1주차 - 3일차"

(defn Regular-Expressions []
  (= "ABC" (apply str (re-seq #"[A-Z]+" "bA1B3Ce "))))

(defn Simple-Recursion []
  (= '(5 4 3 2 1) ((fn foo [x] (when (> x 0) (conj (foo (dec x)) x))) 5)))

(defn Recurring-Theme []
  (= '(7 6 5 4 3)
  (loop [x 5
         result []]
    (if (> x 0)
      (recur (dec x) (conj result (+ 2 x)))
      result))))

(defn Rearranging-Code-> []
  (= (last (sort (rest (reverse [2 5 4 1 3 6]))))
   (-> [2 5 4 1 3 6] (reverse) (rest) (sort) (last))
   5))

(defn Rearranging-Code->> []
  (= (reduce + (map inc (take 3 (drop 2 [2 5 4 1 3 6]))))
     (->> [2 5 4 1 3 6] (drop 2) (take 3) (map inc) (reduce +))
     11))

"For-the-win "

"1주차 - 4일차"

(defn Penultimate-Element []
  ((= (#(second (reverse %)) (list 1 2 3 4 5)) 4)
  (= (#(second (reverse %)) ["a" "b" "c"]) "b")
  (= (#(second (reverse %)) [[1 2] [3 4]]) [1 2])))

(defn Sum-It-All-Up []
  ((= (reduce + [1 2 3]) 6)
  (= (reduce + (list 0 -2 5 5)) 8)
  (= (reduce + #{4 2 1}) 7)
  (= (reduce + '(0 0 -1)) -1)4
  (= (reduce + '(1 10 3)) 14)))

(defn Find-the-odd-numbers []
  ((= (filter even? #{1 2 3 4 5}) '(1 3 5))
  (= (filter even? [4 2 1 6]) '(1))
  (= (filter even? [2 2 4 6]) '())
  (= (filter even? [1 1 1 3]) '(1 1 1 3))))

(defn Palindrome-Detector []
  ((let [sol-fn #(= (reverse (into [] %)) (into [] %))]
    (false? (sol-fn '(1 2 3 4 5)))
    (true? (sol-fn "racecar"))
    (true? (sol-fn [:foo :bar :foo]))
    (true? (sol-fn '(1 1 3 3 1 1)))
    (false? (sol-fn '(:a :b :c))))))

(defn Duplicate-a-Sequence []
  ((let [sol-fn (fn duplicate [target]
                   (if (empty? target)
                     '()
                     (conj (duplicate (rest target)) (first target) (first target))))]
    (= (sol-fn [1 2 3]) '(1 1 2 2 3 3))
    (= (sol-fn [:a :a :b :b]) '(:a :a :a :a :b :b :b :b))
    (= (sol-fn [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4]))
    (= (sol-fn [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4])))))

"1주차 - 5일차"

(defn Compress-a-Sequence []
  ((= (apply str (#(map first (partition-by identity %)) "Leeeeeerrroyyy")) "Leroy")
  (= (#(map first (partition-by identity %)) [1 1 2 3 3 2 2 3]) '(1 2 3 2 3))
  (= (#(map first (partition-by identity %)) [[1 2] [1 2] [3 4] [1 2]]) '([1 2] [3 4] [1 2]))))

(defn Pack-a-Sequence []
  ((= (#(partition-by identity %) [1 1 2 1 1 1 3 3]) '((1 1) (2) (1 1 1) (3 3)))
  (= (#(partition-by identity %) [:a :a :b :b :c]) '((:a :a) (:b :b) (:c)))
  (= (#(partition-by identity %) [[1 2] [1 2] [3 4]]) '(([1 2] [1 2]) ([3 4])))))

(defn Drop-Every-Nth-Item []
  (let [sol-fn (fn [the-vector n]
    (if (empty? the-vector)
      '()
      (concat
       (take (dec n) the-vector)
       (drop-nth-item (drop n the-vector) n))))]
    ((= (__ [1 2 3 4 5 6 7 8] 3) [1 2 4 5 7 8])
    (= (__ [:a :b :c :d :e :f] 2) [:a :c :e])
    (= (__ [1 2 3 4 5 6] 4) [1 2 3 5 6]))))

(defn Intro-to-Iterate []
  (= '(1 4 7 10 13) (take 5 (iterate #(+ 3 %) 1)))

(defn Replicate-a-sequence []
  ((= ((fn [the-list n] (mapcat #(repeat n %) the-list)) [1 2 3] 2) '(1 1 2 2 3 3))
  (= ((fn [the-list n] (mapcat #(repeat n %) the-list)) [:a :b] 4) '(:a :a :a :a :b :b :b :b))
  (= ((fn [the-list n] (mapcat #(repeat n %) the-list)) [4 5 6] 1) '(4 5 6))
  (= ((fn [the-list n] (mapcat #(repeat n %) the-list)) [[1 2] [3 4]] 2) '([1 2] [1 2] [3 4] [3 4]))
  (= ((fn [the-list n] (mapcat #(repeat n %) the-list)) [44 33] 2) [44 44 33 33]))
