(def the-world (ref "hello"))
(def the-universe (ref "goodbye"))
(def bizarro-world (ref {}))


  "In the beginning, there was a word"
  (= "hello" (deref the-world))

  "You can get the word more succintly, but it's the same"
  (= "hello" @the-world)

  "You can be the change you wish to see in the world. -Ghandi"
  (= "better" (do
          (dosync (ref-set the-world "better")
                  (ref-set the-universe @the-world))
          @the-world))
  @the-universe
  @the-world

  "Alter where you need not replace"
  (= "better!!!" (let [exclamator (fn [x] (str x "!"))]
           (dosync
           (alter the-world exclamator)
           (alter the-world exclamator)
           (alter the-world exclamator))
          @the-world))

  "Don't forget to do your work in a transaction!"
  (= 1 (do
         (dosync (ref-set the-world 1))
           @the-world))

  "Functions passed to alter may depend on the data in the ref"
  (= 20 (do
          (dosync (alter the-world #(+ % 19)))))

  "Two worlds are better than one"
  (= ["Real Jerry" "Bizarro Jerry"]
       (do
         (dosync
          (ref-set the-world {})
          (alter the-world assoc :jerry "Real Jerry")
          (alter bizarro-world assoc :jerry "Bizarro Jerry")
          [(:jerry @the-world) (:jerry @bizarro-world)])))
 [(:jerry @the-world) (:jerry @bizarro-world)]
