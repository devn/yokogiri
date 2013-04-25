# yokogiri

In your `project.clj`: `[yokogiri "1.5.1"]`
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
    (def client (yokogiri/make-client))
	
	(def page (yokogiri/get-page client "http://example.com"))
	
	;; xpath
	(def anchor-node-text (map node-text (xpath page "//a")))
	
	;; css
	(def footer-feedback-text
	  (map node-text (css page "div.footer-beta-feedback")))
	  
	;; get specific attributes
	(def a-attr-href
           (map #(select-keys % [:href]) (map attr-map (css page "div.link a"))))  
```
## license

Copyright (C) 2012 Devin Walters

Distributed under the Eclipse Public License, the same as Clojure.
