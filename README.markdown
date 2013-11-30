# yokogiri

## status

[![Build Status](https://travis-ci.org/devn/yokogiri.png)](https://travis-ci.org/devn/yokogiri)

## getting started

In your `project.clj`: `[yokogiri "1.5.4"]`
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

  ;; with javascript enabled (see source for additional options):
  (def client (make-client :javascript true))

  (def page (get-page client "http://example.com"))

  ;; xpath
  (def anchor-node-text (map node-text (xpath page "//a")))

  ;; css
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
