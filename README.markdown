# yokogiri

In your `project.clj`: `[yokogiri "0.0.1"]`

    (ns myproject.core
	  (:require [yokogiri.core :as yokogiri]))

or

	(ns myproject.core
	  (:use [yokogiri.core]))


## Usage

    (def client (yokogiri/make-client))
	
	(def page (yokogiri/get-page client "http://example.com"))
	
	;; xpath
	(def anchor-node-text (map node-text (xpath page "//a")))
	
	;; css
	(def footer-feedback-text
	  (map node-text (css page "div.footer-beta-feedback")))

## License

Copyright (C) 2012 Devin Walters

Distributed under the Eclipse Public License, the same as Clojure.
