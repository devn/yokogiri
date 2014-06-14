# yokogiri

## status

[![Build Status](https://travis-ci.org/devn/yokogiri.png)](https://travis-ci.org/devn/yokogiri)

## getting started

In your `project.clj`: `[yokogiri "1.5.8"]`
```clojure
  (ns myproject.core
    (:require [yokogiri.core :as $]))
```

## usage
```clojure
  (let [client (make-client)])

  ;; with javascript enabled (look at the docstring for make-client
  ;; for all of the available options.):
  (let [client (make-client :javascript false)]
    )

  ;; Curious what options are set by default?
  (get-client-options (make-client))
  ;=> {:redirects true, :javascript true, ...}

  ;; XPATH
  ;; First, we get the page.
  (let [page (get-page client "http://example.com")]
    )

  (xpath page "//a")
  ;=> [#<HtmlAnchor HtmlAnchor[<a href="http://www.iana.org/domains/example">]>]

  (map attrs (xpath page "//a"))
  ;=> ({:text "More information...", :href "http://www.iana.org/domains/example"})

  (map node-text (xpath page "//a"))
  ;=> ("More information...")

  ;; CSS
  (let [footer-feedback-text (map node-text (css page "div.footer-beta-feedback"))]
    )

  ;; Get specific attributes
  (let [a-attr-href (map #(select-keys (attrs %) [:href])
                         (-> page (css "div.link a")))]
    )

  ;; Not necessary to pass around client:
  (get-page "http://example.com/")

  ;; Rebind *client*
  (with-client (make-client :javascript false)
    (get-page "http://www.example.com/"))

  ;; Treat a local HTML file as a page:
  (xpath (as-page "docs/uberdoc.html") "//a")

  ;; Treat an HTML string as a page:
  (let [html-string "<html><body><a href=\"/foo\">bar</a></body></html>"]
    (xpath (create-page html-string) "//a"))
```

## documentation

Check out the [nicely formatted documentation](https://rawgithub.com/devn/yokogiri/master/docs/uberdoc.html).

## license

Copyright (C) 2013 Devin Walters

Distributed under the Eclipse Public License, the same as Clojure.
