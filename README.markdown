# yokogiri

## status

[![Build Status](https://travis-ci.org/devn/yokogiri.png)](https://travis-ci.org/devn/yokogiri)

[![Clojars Project](http://clojars.org/yokogiri/latest-version.svg)](http://clojars.org/yokogiri)

## getting started

In your `project.clj`: `[yokogiri "1.5.8"]`
```clojure
  (ns myproject.core
    (:require [yokogiri.core :as $]))
```

## usage
```clojure
  ;; Require yokogiri
  (ns myproject.core
    (:require [yokogiri.core :as y]))

  ;; Make a client
  (y/make-client)

  ;; with javascript enabled (look at the docstring for make-client
  ;; for all of the available options.):
  (let [a-client (y/make-client :javascript false)]
    (y/get-client-options a-client))

  ;; Curious what options are set by default?
  (y/get-client-options (y/make-client))
  ;=> {:redirects true, :javascript true, ...}

  ;; XPATH && CSS Scraping
  ;; First, we make a client, and get a page.
  (let [client (y/make-client)
        page (y/get-page client "http://example.com")]

    ;; XPATH
    (y/xpath page "//a")
    ;=> [#<HtmlAnchor HtmlAnchor[<a href="http://www.iana.org/domains/example">]>]

    (map y/attrs (y/xpath page "//a"))
    ;=> ({:text "More information...", :href "http://www.iana.org/domains/example"})

    (map y/node-text (y/xpath page "//a"))
    ;=> ("More information...")

    ;; CSS
    (map y/node-text (y/css page "div.footer-beta-feedback"))

    ;; Get specific attributes
    (map #(select-keys (y/attrs %) [:href])
         (y/css page "div.link a")))

  ;; Other Usage Notes:
  ;; We don't *have to* create a client in order to get a page and do stuff with it:
  (y/get-page "http://example.com/")

  ;; Dynamically rebind *client* to get a new, temporary client within a scope:
  (y/with-client (y/make-client :javascript false)
    (y/get-page "http://www.example.com/"))

  ;; Treat a local HTML file as a page:
  (y/xpath (y/as-page "docs/uberdoc.html") "//a")

  ;; Treat an HTML string as a page:
  (let [html-string "<html><body><a href=\"/foo\">bar</a></body></html>"]
    (y/xpath (y/create-page html-string) "//a"))
```

## documentation

Check out the [nicely formatted documentation](https://rawgithub.com/devn/yokogiri/master/docs/uberdoc.html).

## license

Copyright (C) 2013 Devin Walters

Distributed under the Eclipse Public License, the same as Clojure.
