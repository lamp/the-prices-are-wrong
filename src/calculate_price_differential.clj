(ns calculate-price-differential
  (:require [clojure.data.csv :as csv]
            [clojure.string :as string]
            [clojure.java.io :as io]))

(defn useless-directory? [directory?]
  (let [directory-name (str directory?)]
    (or (string/ends-with? directory-name ".csv")
        (= "data" directory-name)
        (= (count (string/split directory-name #"/")) 2))))


(defn load-data [prices-csv-path receipts-directory]
  (let [prices-csv (with-open [reader (io/reader prices-csv-path)]
                     (doall (csv/read-csv reader)))
        receipts (->> receipts-directory
                      io/file
                      file-seq
                      (remove useless-directory?)
                      (map slurp))]
    [prices-csv receipts]))

(defn ->prices-by-product-id [prices]
  (reduce (fn [acc [key value]]
            (assoc acc key value))
          {}
          prices))

(defn ->receipts-by-store-id [receipts]
  (reduce (fn [receipts receipt]
            (let [receipt-lines (string/split-lines receipt)
                  store-id (-> receipt-lines
                               first
                               (string/split #"\#")
                               second
                               string/trim)]
              (->> receipt-lines
                   rest
                   (reduce (fn [acc r]
                             (let [trimmed-line (string/trim r)]
                               (cond
                                 (or (empty? trimmed-line)
                                     (string/starts-with? trimmed-line "TOTAL")) acc
                                 (string/starts-with? trimmed-line "*") (butlast acc)
                                 :else (let [trim-line-item (comp (remove empty?)
                                                                  (map clojure.string/trim))
                                             formatted-line-item (into [] trim-line-item (string/split r #"\s{2,3}"))
                                             [product-name product-code price & _] formatted-line-item]
                                         (conj
                                          acc
                                          {:name product-name
                                           :code product-code
                                           :price (-> price
                                                      (string/split #"\s")
                                                      first)})))))
                           [])
                   (update receipts store-id concat))))
          {} receipts))

(defn build-plus-minus [prices-by-product-id receipts-by-store-id]
  (map (fn [[store-id line-items]]
         [store-id (reduce (fn [plus-minus line-item]
                             (let [stated-price (->> line-item
                                                     :code
                                                     (get prices-by-product-id)
                                                     Float/parseFloat)
                                   price-paid (-> line-item :price Float/parseFloat)]

                               (if-not (= stated-price price-paid)
                                 (+ plus-minus (- price-paid stated-price))
                                 plus-minus)))
                           0
                           line-items)])
       receipts-by-store-id))


(defn -main []
  (let [[prices-raw receipts-raw] (load-data "data/prices.csv" "data")
        prices-by-product-id (->prices-by-product-id prices-raw)
        receipts-by-store-id (->receipts-by-store-id receipts-raw)
        output (->> receipts-by-store-id
                   (build-plus-minus prices-by-product-id)
                   (sort-by #(-> % second Math/abs))
                   reverse
                   (cons ["store" "plusminus"]))]

    (with-open [writer (io/writer "plusminus.csv")]
      (csv/write-csv writer output))))

(-main)
