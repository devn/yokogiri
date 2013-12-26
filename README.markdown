# yokogiri

## status

[![Build Status](https://travis-ci.org/devn/yokogiri.png)](https://travis-ci.org/devn/yokogiri)

## getting started

In your `project.clj`: `[yokogiri "1.5.5"]`
```clojure
  (ns myproject.core
    (:require [yokogiri.core :as yokogiri]))
```
or
```clojure
  (ns myproject.core
    (:use [yokogiri.core]))
```

## usage
```clojure
  (def client (make-client))

  ;; with javascript enabled (look at the docstring for make-client
  ;; for all of the available options.):
  (def client (make-client :javascript true))

  ;; Curious what options are set by default?
  (get-client-options (make-client))
  ;=> {:redirects true, :javascript true, ...}

  ;; XPATH
  ;; First, we get the *page*.
  (def page (get-page client "http://example.com"))

  (xpath page "//a")
  ;=> [#<HtmlAnchor HtmlAnchor[<a href="http://www.iana.org/domains/example">]>]

  (map attrs (xpath page "//a"))
  ;=> ({:text "More information...", :href "http://www.iana.org/domains/example"})

  (map node-text (xpath page "//a"))
  ;=> ("More information...")

  ;; CSS
  (def footer-feedback-text
    (map node-text (css page "div.footer-beta-feedback")))

  ;; get specific attributes
  (def a-attr-href
    (map #(select-keys (attr-map %) [:href])
      (-> page css "div.link a")))
```

## license

Copyright (C) 2013 Devin Walters

Distributed under the Eclipse Public License, the same as Clojure.
