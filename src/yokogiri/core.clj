(ns yokogiri.core
  (:import [com.gargoylesoftware.htmlunit WebClient BrowserVersion]
           [com.gargoylesoftware.htmlunit.html HtmlPage DomNode DomAttr]
           [org.w3c.dom NamedNodeMap Node]
           [se.fishtank.css.selectors.dom DOMNodeSelector]))

(set! *warn-on-reflection* true)

(defn make-client
  "Constructs a new WebClient"
  []
  (new WebClient))

(defn get-page
  "GET a url"
  [^WebClient client, ^String url]
  (.getPage client url))

(defn xpath
  "Return nodes matching a given path"
  [^HtmlPage page, ^String xpath]
  (.getByXPath page xpath))

(defn first-by-xpath
  "Find the first node matching a given xpath"
  [^HtmlPage page, ^String xpath]
  (.getFirstByXPath page xpath))

;; http://www.goodercode.com/wp/use-css-selectors-with-htmlunit/
;; http://stackoverflow.com/questions/9275467/converting-java-collections-to-clojure-data-structures
(defn css
  "Returns matches for a given CSS selector

  Usage:
  (css (visit *client* \"http://google.com\") \"a.gbzt\")
  ;=> (#<HtmlAnchor HtmlAnchor[<a onclick\"gbar.logger.il(1,{t:1}); ...)"
  [^HtmlPage page, ^String selector]
  (let [queryable-page (DOMNodeSelector. (. page getDocumentElement))]
    (seq (. queryable-page querySelectorAll selector))))

(defn node-xml
  "Returns a node's xml representation"
  [^DomNode node]
  (.asXml node))

(defn node-text
  "Returns a node's text value

  Usage:
  user> (node-text #<HtmlAnchor HtmlAnchor[<a class=\"foo\" id=\"bar\" href=\"http://example.com\">]>)
  ;=> \"Search\""
  [^DomNode node]
  (.asText node))

(defn attr-map
  "Returns a clojure map of attributes for a given node

  Usage:
  user> (attr-map #<HtmlAnchor HtmlAnchor[<a class=\"foo\" id=\"bar\" href=\"http://example.com\">]>)
  ;=> {:text \"Search\", :href \"http://example.com\", :id \"bar\", :class \"foo\"}"
  [^Node node]
  (let [^NamedNodeMap attrs (.getAttributes node)]
    (loop [acc 0, res {}]
      (if (= acc (count attrs))
        (assoc res :text (node-text node))
        (recur (inc acc)
               (let [^DomAttr attr (.item attrs acc)]
                 (assoc res (keyword (.getName attr)) (.getValue attr))))))))

(defn- dom-attr
  "Returns the HtmlUnit DomAttr objects for a given node

  See: yokogiri.core/attr-map"
  [^DomNode node]
  (let [^NamedNodeMap attrs (.getAttributes node)
        len (.getLength attrs)]
    (map #(.item attrs %) (range 0 len))))


;;******************************************************************************

(comment ;; Development Switches
  (set! *warn-on-reflection* true))

(comment ;; TODO: Add arity
  (def browser-versions
    {"IE6" BrowserVersion/INTERNET_EXPLORER_6
     "IE7" BrowserVersion/INTERNET_EXPLORER_7
     "IE8" BrowserVersion/INTERNET_EXPLORER_8
     "FF3" BrowserVersion/FIREFOX_3})

  (defn make-client
    ([] (make-client (.getNickname (BrowserVersion/getDefault))))
    ([version] (let [vers ((.toUpperCase version) browser-versions)]
                 (new WebClient ,,,))))
  )

(comment ;; Trying something else instead...
  (ns yokogiri.core
    (:import [org.cyberneko.html.parsers DOMParser]
             [org.w3c.dom HTMLDocumentImpl]))
  (def parser
    (new DOMParser))
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
  )

(comment ;; Maybe an API like this...
 (defn with-client [c f & args]
   (apply #(f c %) args))
 
(defn visit [^String url]
  (. *client* getPage url))

 (def c (make-client))
 (with-client c visit "http://www.google.com")
)

(comment ;; Maybe an API like this...
  (defn scrape
    [url & opts]
    {:pre [(even? (count opts))]}
    (let [opts (apply hash-map opts)
          page (. *client* getPage url)
          ks (keys opts)]
      ,,,use opts to call functions on the return of page IN ORDER (vector probably),,,))

  (map node-text (scrape "http://www.google.com" :xpath "//a"))
  (map node-text (scrape "http://www.google.com" [:xpath "//a"])))
