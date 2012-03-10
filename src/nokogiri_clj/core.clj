(ns nokogiri-clj.core
  (:import [org.cyberneko.html.parsers DOMParser]
           [org.w3c.dom Document Node]))

(comment
  "==Development Switches=="
  (set! *warn-on-reflection* true))

(def parser (new DOMParser))

(def parsed-url
  (. parser parse "http://clojure-log.n01se.net/date/2008-02-01.html"))

(def document
  (. parser getDocument))

(defn get-element-by-id
  [^org.apache.html.dom.HTMLDocumentImpl doc ^String id]
  (. doc getElementById id))

(defn get-elements-by-tag-name
  [^org.apache.html.dom.HTMLDocumentImpl doc ^String tag]
  (. doc getElementsByTagName tag))