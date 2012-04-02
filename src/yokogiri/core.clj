(ns yokogiri.core
  (:import [com.gargoylesoftware.htmlunit WebClient BrowserVersion]
           [com.gargoylesoftware.htmlunit.html HtmlPage]
           [se.fishtank.css.selectors.dom DOMNodeSelector]))

(defn make-client []
  (new WebClient))

(def ^:dynamic *client* (make-client))

(defn visit [^WebClient c, ^String url]
  (. c getPage url))

(defn xpath [^HtmlPage page, ^String xpath]
  (. page getByXPath xpath))

;; http://www.goodercode.com/wp/use-css-selectors-with-htmlunit/
;; http://stackoverflow.com/questions/9275467/converting-java-collections-to-clojure-data-structures
(defn css [^HtmlPage page, ^String selector]
  (let [queryable-page (DOMNodeSelector. (. page getDocumentElement))]
    (seq (. queryable-page querySelectorAll selector))))

(defn node-xml [node]
  (.asXml node))

(defn node-text [node]
  (.asText node))

(defn attrs [node]
  (let [attrs (.getAttributes node)]
    (loop [acc 0, res {}]
      (if (= acc (count attrs))
        (assoc res :text (node-text node))
        (recur (inc acc)
               (let [attr (.item attrs acc)]
                 (assoc res (keyword (.getName attr)) (.getValue attr))))))))


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
