(ns yokogiri.test.core
  (:use [yokogiri.core])
  (:use [midje.sweet]))

(fact "These tests need to be improved, but they're better than nothing."
  true => true)

(facts "yokogiri.core/make-client"
  (fact "It can create a client"
    (let [simple-client (make-client)]
      simple-client
      => truthy
      (class simple-client)
      => com.gargoylesoftware.htmlunit.WebClient))

  (fact "It can create a client with options"
    (let [insecure-client (make-client :insecure-ssl true)]
      (:insecure-ssl (get-client-options insecure-client))
      => true))
  (fact "It can create a client with many non-default options"
    (let [c (make-client :insecure-ssl false
                         :javascript false
                         :css false)
          opts (get-client-options c)]
      (->> (-> opts
               (select-keys [:insecure-ssl :javascript :css])
               (vals))
           (every? false?)))
           => true))

(facts "About CSS and XPath queries"
  (let [c (make-client)
        p (get-page c "http://www.example.com/")]
    (fact "CSS works"
      (css p "a")
      => truthy)
    (fact "XPath works")
      (xpath p "//a")
      => truthy))
