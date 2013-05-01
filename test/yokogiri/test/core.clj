(ns yokogiri.test.core
  (:use [yokogiri.core])
  (:use [midje.sweet]))

(facts "about clients"
  (make-client) => truthy)
