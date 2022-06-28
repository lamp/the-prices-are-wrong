(ns calculate-price-differential-test
  (:require [clojure.test :refer [deftest testing is run-tests]]
            [calculate-price-differential :as sut]
            [clojure.string :as string]))


(deftest remove-useless-directories-test
  (testing "Returns false if it does not match the pattern"
    (is (false?  (sut/useless-directory? "data/1900-01-01/45678908"))))
  (testing "Returns true if path matches"
    (is (true? (sut/useless-directory? "data")))
    (is (true? (sut/useless-directory? "data/2018-01-01")))))

(deftest ->prices-by-product-id-test
  (testing "builds prices state"
    (let [prices (sut/->prices-by-product-id [["12234334" "129.90"]])]
      (is (= {"12234334" "129.90"}
             prices)))))

(deftest ->receipts-by-store-id-test
 (testing "builds receipts state"
  (let [receipts (sut/->receipts-by-store-id
                   ["BIGCO  STORE #4                              \nJET FUEL   7975490736                144.91 Z\nBANANAS   0879898868                 192.49  \n*** VOIDED PRODUCT 0879898868 ***            \nPOTATO-CHIPS   1604898223            180.31  \nBLUE MAN RAY DISK   6784960106       157.29 F\nDIRED   6386159067                   141.23  \nHAPPY CAT   2165001760               173.69 F\n*** VOIDED PRODUCT 2165001760 ***            \nSICP   8649139606                    196.66 X\nCIDER   3511092833                    87.58  \nREANIMATEDMCCARTHY   7780854831       68.17 X\n*** VOIDED PRODUCT 7780854831 ***            \nLOTTERY-TICKET$   4651861144         126.00 X\nDAVID NOLENS HAIR   5329799687        67.12  \n*** VOIDED PRODUCT 5329799687 ***            \n16CANDLESDVD   2807441633            103.24  \nSTOLEN-CREDIT-CARD   2925648759       15.08  \nDIRED   6386159067                   141.23 F\nSTOLEN-CREDIT-CARD   2925648759       40.25 Z\nBINARY_XMAS__TREE   7268888904        37.81  \n*** VOIDED PRODUCT 7268888904 ***
                                              \nDAVID NOLENS HAIR   5329799687        33.72 X\nPOTATO CHIPS   8613530819              9.53  \nCIDER   3511092833                    87.58 Z\n*** VOIDED PRODUCT 3511092833 ***            \nCIDER   3511092833                    44.59 Z\nBANANAS   0879898868                  65.48  \nSTALLMANS TOE   5157226892           149.72  \n*** VOIDED PRODUCT 5157226892 ***            \nNEW_TWEETS_BUTTON   5460609671         2.30  \nBINARY_XMAS__TREE   7268888904       248.44  \nLISP-1   9650481305                  195.44 F\nDESK   2581192686                    140.88 X\n                                TOTAL 2074.16"])]
    (is (= {"4"
            [{:name "DESK", :code "2581192686", :price "140.88"}
             {:name "LISP-1", :code "9650481305", :price "195.44"}
             {:name "BINARY_XMAS__TREE", :code "7268888904", :price "248.44"}
             {:name "NEW_TWEETS_BUTTON", :code "5460609671", :price "2.30"}
             {:name "STALLMANS TOE", :code "5157226892", :price "149.72"}
             {:name "BANANAS", :code "0879898868", :price "65.48"}
             {:name "CIDER", :code "3511092833", :price "44.59"}
             {:name "CIDER", :code "3511092833", :price "87.58"}
             {:name "POTATO CHIPS", :code "8613530819", :price "9.53"}
             {:name "DAVID NOLENS HAIR", :code "5329799687", :price "33.72"}
             {:name "BINARY_XMAS__TREE", :code "7268888904", :price "37.81"}
             {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "40.25"}
             {:name "DIRED", :code "6386159067", :price "141.23"}
             {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "15.08"}
             {:name "16CANDLESDVD", :code "2807441633", :price "103.24"}
             {:name "DAVID NOLENS HAIR", :code "5329799687", :price "67.12"}
             {:name "LOTTERY-TICKET$", :code "4651861144", :price "126.00"}
             {:name "REANIMATEDMCCARTHY", :code "7780854831", :price "68.17"}
             {:name "CIDER", :code "3511092833", :price "87.58"}]}
          receipts))))
 (testing "builds multiple receipts"
   (let [receipts (sut/->receipts-by-store-id ["BIGCO  STORE #4                              \nJET FUEL   7975490736                144.91 Z\nBANANAS   0879898868                 192.49  \n*** VOIDED PRODUCT 0879898868 ***            \nPOTATO-CHIPS   1604898223            180.31  \nBLUE MAN RAY DISK   6784960106       157.29 F\nDIRED   6386159067                   141.23  \nHAPPY CAT   2165001760               173.69 F\n*** VOIDED PRODUCT 2165001760 ***            \nSICP   8649139606                    196.66 X\nCIDER   3511092833                    87.58  \nREANIMATEDMCCARTHY   7780854831       68.17 X\n*** VOIDED PRODUCT 7780854831 ***            \nLOTTERY-TICKET$   4651861144         126.00 X\nDAVID NOLENS HAIR   5329799687        67.12  \n*** VOIDED PRODUCT 5329799687 ***            \n16CANDLESDVD   2807441633            103.24  \nSTOLEN-CREDIT-CARD   2925648759       15.08  \nDIRED   6386159067                   141.23 F\nSTOLEN-CREDIT-CARD   2925648759       40.25 Z\nBINARY_XMAS__TREE   7268888904        37.81  \n*** VOIDED PRODUCT 7268888904 ***
                                              \nDAVID NOLENS HAIR   5329799687        33.72 X\nPOTATO CHIPS   8613530819              9.53  \nCIDER   3511092833                    87.58 Z\n*** VOIDED PRODUCT 3511092833 ***            \nCIDER   3511092833                    44.59 Z\nBANANAS   0879898868                  65.48  \nSTALLMANS TOE   5157226892           149.72  \n*** VOIDED PRODUCT 5157226892 ***            \nNEW_TWEETS_BUTTON   5460609671         2.30  \nBINARY_XMAS__TREE   7268888904       248.44  \nLISP-1   9650481305                  195.44 F\nDESK   2581192686                    140.88 X\n                                TOTAL 2074.16"
                                               "BIGCO  STORE #4                              \nJET FUEL   7975490736                144.91 Z\nBANANAS   0879898868                 192.49  \n*** VOIDED PRODUCT 0879898868 ***            \nPOTATO-CHIPS   1604898223            180.31  \nBLUE MAN RAY DISK   6784960106       157.29 F\nDIRED   6386159067                   141.23  \nHAPPY CAT   2165001760               173.69 F\n*** VOIDED PRODUCT 2165001760 ***            \nSICP   8649139606                    196.66 X\nCIDER   3511092833                    87.58  \nREANIMATEDMCCARTHY   7780854831       68.17 X\n*** VOIDED PRODUCT 7780854831 ***            \nLOTTERY-TICKET$   4651861144         126.00 X\nDAVID NOLENS HAIR   5329799687        67.12  \n*** VOIDED PRODUCT 5329799687 ***            \n16CANDLESDVD   2807441633            103.24  \nSTOLEN-CREDIT-CARD   2925648759       15.08  \nDIRED   6386159067                   141.23 F\nSTOLEN-CREDIT-CARD   2925648759       40.25 Z\nBINARY_XMAS__TREE   7268888904        37.81  \n*** VOIDED PRODUCT 7268888904 ***
                                              \nDAVID NOLENS HAIR   5329799687        33.72 X\nPOTATO CHIPS   8613530819              9.53  \nCIDER   3511092833                    87.58 Z\n*** VOIDED PRODUCT 3511092833 ***            \nCIDER   3511092833                    44.59 Z\nBANANAS   0879898868                  65.48  \nSTALLMANS TOE   5157226892           149.72  \n*** VOIDED PRODUCT 5157226892 ***            \nNEW_TWEETS_BUTTON   5460609671         2.30  \nBINARY_XMAS__TREE   7268888904       248.44  \nLISP-1   9650481305                  195.44 F\nDESK   2581192686                    140.88 X\n                                TOTAL 2074.16"])]
    (is (=
          {"4"
           [{:name "DESK", :code "2581192686", :price "140.88"}
            {:name "LISP-1", :code "9650481305", :price "195.44"}
            {:name "BINARY_XMAS__TREE", :code "7268888904", :price "248.44"}
            {:name "NEW_TWEETS_BUTTON", :code "5460609671", :price "2.30"}
            {:name "STALLMANS TOE", :code "5157226892", :price "149.72"}
            {:name "BANANAS", :code "0879898868", :price "65.48"}
            {:name "CIDER", :code "3511092833", :price "44.59"}
            {:name "CIDER", :code "3511092833", :price "87.58"}
            {:name "POTATO CHIPS", :code "8613530819", :price "9.53"}
            {:name "DAVID NOLENS HAIR", :code "5329799687", :price "33.72"}
            {:name "BINARY_XMAS__TREE", :code "7268888904", :price "37.81"}
            {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "40.25"}
            {:name "DIRED", :code "6386159067", :price "141.23"}
            {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "15.08"}
            {:name "16CANDLESDVD", :code "2807441633", :price "103.24"}
            {:name "DAVID NOLENS HAIR", :code "5329799687", :price "67.12"}
            {:name "LOTTERY-TICKET$", :code "4651861144", :price "126.00"}
            {:name "REANIMATEDMCCARTHY", :code "7780854831", :price "68.17"}
            {:name "CIDER", :code "3511092833", :price "87.58"}
            {:name "DESK", :code "2581192686", :price "140.88"}
            {:name "LISP-1", :code "9650481305", :price "195.44"}
            {:name "BINARY_XMAS__TREE", :code "7268888904", :price "248.44"}
            {:name "NEW_TWEETS_BUTTON", :code "5460609671", :price "2.30"}
            {:name "STALLMANS TOE", :code "5157226892", :price "149.72"}
            {:name "BANANAS", :code "0879898868", :price "65.48"}
            {:name "CIDER", :code "3511092833", :price "44.59"}
            {:name "CIDER", :code "3511092833", :price "87.58"}
            {:name "POTATO CHIPS", :code "8613530819", :price "9.53"}
            {:name "DAVID NOLENS HAIR", :code "5329799687", :price "33.72"}
            {:name "BINARY_XMAS__TREE", :code "7268888904", :price "37.81"}
            {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "40.25"}
            {:name "DIRED", :code "6386159067", :price "141.23"}
            {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "15.08"}
            {:name "16CANDLESDVD", :code "2807441633", :price "103.24"}
            {:name "DAVID NOLENS HAIR", :code "5329799687", :price "67.12"}
            {:name "LOTTERY-TICKET$", :code "4651861144", :price "126.00"}
            {:name "REANIMATEDMCCARTHY", :code "7780854831", :price "68.17"}
            {:name "CIDER", :code "3511092833", :price "87.58"}]}
          receipts))))
 (testing "it handles multiple stores"
   (let [receipts (sut/->receipts-by-store-id ["BIGCO  STORE #4                              \nJET FUEL   7975490736                144.91 Z\nBANANAS   0879898868                 192.49  \n*** VOIDED PRODUCT 0879898868 ***            \nPOTATO-CHIPS   1604898223            180.31  \nBLUE MAN RAY DISK   6784960106       157.29 F\nDIRED   6386159067                   141.23  \nHAPPY CAT   2165001760               173.69 F\n*** VOIDED PRODUCT 2165001760 ***            \nSICP   8649139606                    196.66 X\nCIDER   3511092833                    87.58  \nREANIMATEDMCCARTHY   7780854831       68.17 X\n*** VOIDED PRODUCT 7780854831 ***            \nLOTTERY-TICKET$   4651861144         126.00 X\nDAVID NOLENS HAIR   5329799687        67.12  \n*** VOIDED PRODUCT 5329799687 ***            \n16CANDLESDVD   2807441633            103.24  \nSTOLEN-CREDIT-CARD   2925648759       15.08  \nDIRED   6386159067                   141.23 F\nSTOLEN-CREDIT-CARD   2925648759       40.25 Z\nBINARY_XMAS__TREE   7268888904        37.81  \n*** VOIDED PRODUCT 7268888904 ***
                                              \nDAVID NOLENS HAIR   5329799687        33.72 X\nPOTATO CHIPS   8613530819              9.53  \nCIDER   3511092833                    87.58 Z\n*** VOIDED PRODUCT 3511092833 ***            \nCIDER   3511092833                    44.59 Z\nBANANAS   0879898868                  65.48  \nSTALLMANS TOE   5157226892           149.72  \n*** VOIDED PRODUCT 5157226892 ***            \nNEW_TWEETS_BUTTON   5460609671         2.30  \nBINARY_XMAS__TREE   7268888904       248.44  \nLISP-1   9650481305                  195.44 F\nDESK   2581192686                    140.88 X\n                                TOTAL 2074.16"
                                               "BIGCO  STORE #5                              \nJET FUEL   7975490736                144.91 Z\nBANANAS   0879898868                 192.49  \n*** VOIDED PRODUCT 0879898868 ***            \nPOTATO-CHIPS   1604898223            180.31  \nBLUE MAN RAY DISK   6784960106       157.29 F\nDIRED   6386159067                   141.23  \nHAPPY CAT   2165001760               173.69 F\n*** VOIDED PRODUCT 2165001760 ***            \nSICP   8649139606                    196.66 X\nCIDER   3511092833                    87.58  \nREANIMATEDMCCARTHY   7780854831       68.17 X\n*** VOIDED PRODUCT 7780854831 ***            \nLOTTERY-TICKET$   4651861144         126.00 X\nDAVID NOLENS HAIR   5329799687        67.12  \n*** VOIDED PRODUCT 5329799687 ***            \n16CANDLESDVD   2807441633            103.24  \nSTOLEN-CREDIT-CARD   2925648759       15.08  \nDIRED   6386159067                   141.23 F\nSTOLEN-CREDIT-CARD   2925648759       40.25 Z\nBINARY_XMAS__TREE   7268888904        37.81  \n*** VOIDED PRODUCT 7268888904 ***
                                              \nDAVID NOLENS HAIR   5329799687        33.72 X\nPOTATO CHIPS   8613530819              9.53  \nCIDER   3511092833                    87.58 Z\n*** VOIDED PRODUCT 3511092833 ***            \nCIDER   3511092833                    44.59 Z\nBANANAS   0879898868                  65.48  \nSTALLMANS TOE   5157226892           149.72  \n*** VOIDED PRODUCT 5157226892 ***            \nNEW_TWEETS_BUTTON   5460609671         2.30  \nBINARY_XMAS__TREE   7268888904       248.44  \nLISP-1   9650481305                  195.44 F\nDESK   2581192686                    140.88 X\n                                TOTAL 2074.16"])]
    (is (=
          {"4"
           [{:name "DESK", :code "2581192686", :price "140.88"}
            {:name "LISP-1", :code "9650481305", :price "195.44"}
            {:name "BINARY_XMAS__TREE", :code "7268888904", :price "248.44"}
            {:name "NEW_TWEETS_BUTTON", :code "5460609671", :price "2.30"}
            {:name "STALLMANS TOE", :code "5157226892", :price "149.72"}
            {:name "BANANAS", :code "0879898868", :price "65.48"}
            {:name "CIDER", :code "3511092833", :price "44.59"}
            {:name "CIDER", :code "3511092833", :price "87.58"}
            {:name "POTATO CHIPS", :code "8613530819", :price "9.53"}
            {:name "DAVID NOLENS HAIR", :code "5329799687", :price "33.72"}
            {:name "BINARY_XMAS__TREE", :code "7268888904", :price "37.81"}
            {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "40.25"}
            {:name "DIRED", :code "6386159067", :price "141.23"}
            {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "15.08"}
            {:name "16CANDLESDVD", :code "2807441633", :price "103.24"}
            {:name "DAVID NOLENS HAIR", :code "5329799687", :price "67.12"}
            {:name "LOTTERY-TICKET$", :code "4651861144", :price "126.00"}
            {:name "REANIMATEDMCCARTHY", :code "7780854831", :price "68.17"}
            {:name "CIDER", :code "3511092833", :price "87.58"}]
           "5"
           [{:name "DESK", :code "2581192686", :price "140.88"}
            {:name "LISP-1", :code "9650481305", :price "195.44"}
            {:name "BINARY_XMAS__TREE", :code "7268888904", :price "248.44"}
            {:name "NEW_TWEETS_BUTTON", :code "5460609671", :price "2.30"}
            {:name "STALLMANS TOE", :code "5157226892", :price "149.72"}
            {:name "BANANAS", :code "0879898868", :price "65.48"}
            {:name "CIDER", :code "3511092833", :price "44.59"}
            {:name "CIDER", :code "3511092833", :price "87.58"}
            {:name "POTATO CHIPS", :code "8613530819", :price "9.53"}
            {:name "DAVID NOLENS HAIR", :code "5329799687", :price "33.72"}
            {:name "BINARY_XMAS__TREE", :code "7268888904", :price "37.81"}
            {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "40.25"}
            {:name "DIRED", :code "6386159067", :price "141.23"}
            {:name "STOLEN-CREDIT-CARD", :code "2925648759", :price "15.08"}
            {:name "16CANDLESDVD", :code "2807441633", :price "103.24"}
            {:name "DAVID NOLENS HAIR", :code "5329799687", :price "67.12"}
            {:name "LOTTERY-TICKET$", :code "4651861144", :price "126.00"}
            {:name "REANIMATEDMCCARTHY", :code "7780854831", :price "68.17"}
            {:name "CIDER", :code "3511092833", :price "87.58"}]}
          receipts)))))


(deftest build-plus-minus-test
  (testing "it handles the prices being correct"
    (is (= [["1" 0]]
           (sut/build-plus-minus {"1234" "100.00"} {"1" [{:name "foo" :price "100.00" :code "1234"}]}))))
  (testing "it handles prices being lower than stated"
    (is (= [["1" -100.0]]
          (sut/build-plus-minus {"1234" "200.00"} {"1" [{:name "foo" :price "100.00" :code "1234"}]}))))
  (testing "it handles prices being higher than stated"
    (is (= [["1" 100.0]]
          (sut/build-plus-minus {"1234" "100.00"} {"1" [{:name "foo" :price "200.00" :code "1234"}]}))))
  (testing "it handles multiple products in multiple stores"
    (is (= [["1" 250.0] ["2" -37.0]]
           (sut/build-plus-minus
             {"1234" "100.00" "5678" "25.00"}
             {"1" [{:code "5678" :price "75"} {:code "1234" :price "300.00"}]
              "2" [{:code "5678" :price "13"} {:code "1234" :price "75.0"}]})))))
