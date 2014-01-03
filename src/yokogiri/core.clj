(ns yokogiri.core
  (:import [com.gargoylesoftware.htmlunit WebClient BrowserVersion WebClientOptions]
           [com.gargoylesoftware.htmlunit.html HtmlPage DomNode DomAttr]
           [org.w3c.dom NamedNodeMap Node]
           [se.fishtank.css.selectors.dom DOMNodeSelector]))

(set! *warn-on-reflection* true)

(defn- web-client-options
  "Returns the client options object for a WebClient."
  [^WebClient client] (.getOptions client))

(def set-client-options-map
  {:activex-native                  #(.setActiveXNative                     ^WebClientOptions %1 %2)
   :applet                          #(.setAppletEnabled                     ^WebClientOptions %1 %2)
   :block-popups                    #(.setPopupBlockerEnabled               ^WebClientOptions %1 %2)
   :css                             #(.setCssEnabled                        ^WebClientOptions %1 %2)
   :geolocation                     #(.setGeolocationEnabled                ^WebClientOptions %1 %2)
   :homepage                        #(.setHomePage                          ^WebClientOptions %1 %2)
   :insecure-ssl                    #(.setUseInsecureSSL                    ^WebClientOptions %1 %2)
   :print-content-on-failing-status #(.setPrintContentOnFailingStatusCode   ^WebClientOptions %1 %2)
   :redirects                       #(.setRedirectEnabled                   ^WebClientOptions %1 %2)
   :throw-on-failing-status         #(.setThrowExceptionOnFailingStatusCode ^WebClientOptions %1 %2)
   :throw-on-script-error           #(.setThrowExceptionOnScriptError       ^WebClientOptions %1 %2)
   :timeout                         #(.setTimeout                           ^WebClientOptions %1 %2)
   :tracking                        #(.setDoNotTrackEnabled                 ^WebClientOptions %1 %2)
   :javascript                      #(.setJavaScriptEnabled                 ^WebClientOptions %1 %2)})

(defn set-client-options!
  "Sets options on the client.

  **Usage:**

    (let [client (make-client)]
      (set-client-options! client {:redirects false}))
    ;=> #<WebClient com.gargoylesoftware.htmlunit.WebClient@7622ccf2>

  **Available Options:**

    :activex-native                   bool
    :applet                           bool
    :css                              bool
    :geolocation                      bool
    :insecure-ssl                     bool
    :print-content-on-failing-status  bool
    :redirects                        bool
    :throw-on-failing-status          bool
    :throw-on-script-error            bool
    :tracking                         bool
    :javascript                       bool
    :homepage                         string
    :timeout                          integer"
  [^WebClient client opts]
  (let [^WebClientOptions client-opts (web-client-options client)]
    (doseq [[k v] opts]
      (let [setter-fn (get set-client-options-map k)]
        (setter-fn client-opts v)))
    client))

(defn get-client-options
  "Returns a map of all options currently set on a client.

  **Usage:**

    user> (let [client (make-client :redirects false)]
            (get-client-options client))
    ;=> {:javascript true, :redirects false, ...}"
  [^WebClient client]
  (let [^WebClientOptions opts (web-client-options ^WebClient client)]
    {:activex-native                       (. opts isActiveXNative)
     :applet                               (. opts isAppletEnabled)
     :block-popups                         (. opts isPopupBlockerEnabled)
     :css                                  (. opts isCssEnabled)
     :gelocation                           (. opts isGeolocationEnabled)
     :homepage                             (. opts getHomePage)
     :insecure-ssl                         (. opts isUseInsecureSSL)
     :javascript                           (. opts isJavaScriptEnabled)
     :print-content-on-failing-status-code (. opts getPrintContentOnFailingStatusCode)
     :redirects                            (. opts isRedirectEnabled)
     :throw-on-failing-status              (. opts isThrowExceptionOnFailingStatusCode)
     :throw-on-script-error                (. opts isThrowExceptionOnScriptError)
     :timeout                              (. opts getTimeout)
     :tracking                             (. opts isDoNotTrackEnabled)}))

(defn make-client
  "Constructs a new WebClient.

  **Usage:**

    user> (make-client)
    ;=> #<WebClient com.gargoylesoftware.htmlunit.WebClient@124d43a8>

  **With Options:**

    user> (make-client :geolocation true
                       :block-popups false)
    ;=> #<WebClient com.gargoylesoftware.htmlunit.WebClient@4473f04f>

  **Available Options:**

    :activex-native                   bool
    :applet                           bool
    :css                              bool
    :geolocation                      bool
    :insecure-ssl                     bool
    :print-content-on-failing-status  bool
    :redirects                        bool
    :throw-on-failing-status          bool
    :throw-on-script-error            bool
    :tracking                         bool
    :javascript                       bool
    :homepage                         string
    :timeout                          integer

  _See also: yokogiri.core/set-client-options!_"
  [& {:as opts}]
  (let [client (new WebClient)]
    (if-not (empty? opts)
      (set-client-options! (new WebClient) opts)
      client)))

(defn get-page
  "Takes a client and a url, returns an HtmlPage.

  **Usage:**

    user> (get-page (make-client) \"http://www.example.com/\")
    ;=> #<HtmlPage HtmlPage(http://www.example.com/)@478170219>"
  [^WebClient client, ^String url]
  (.getPage ^WebClient client url))

(defn xpath
  "Takes an HtmlPage and an xpath string. Returns a vector of nodes
  which match the provided xpath string.

  **Usage:**

    user> (let [page (get-page your-client \"http://www.example.com\")]
            (xpath page \"//a\"))
    ;=> [#<HtmlAnchor HtmlAnchor[<a href=\"http://www.iana.org/domains/example\">]>]"
  [^HtmlPage page, ^String xpath]
  (into [] (.getByXPath page xpath)))

(defn first-by-xpath
  "Takes an HtmlPage and an xpath string. Returns the first matching
  node which matches the provided xpath string.

  **Usage:**

    user> (first-by-xpath
            (get-page your-client \"http://www.example.com/\")
            \"//a\")
    ;=> #<HtmlAnchor HtmlAnchor[<a href=\"http://www.iana.org/domains/example\">]>"
  [^HtmlPage page, ^String xpath]
  (.getFirstByXPath page xpath))

;; _http://www.goodercode.com/wp/use-css-selectors-with-htmlunit/_
;; _TODO: Bumping the version of css-selectors to 1.0.4 breaks_
;; _querying by CSS._
(defn css
  "Returns matches for a given CSS selector

  **Usage:**

    user> (css your-client \"a.gbzt\")
    ;=> (#<HtmlAnchor HtmlAnchor[<a onclick...>]>, ...)"
  [^HtmlPage page, ^String selector]
  (let [queryable-page (DOMNodeSelector. (. page getDocumentElement))]
    (seq (. queryable-page querySelectorAll selector))))

(defn node-xml
  "Returns a node's XML representation.

  **Usage:**

    user> (node-xml
            (first-by-xpath
              (get-page (make-client) \"http://www.example.com/\")
             \"//a\"))
    ;=> <a href=\"http://www.iana.org/domains/example\">\n  More information...\n</a>\n"
  [^DomNode node]
  (.asXml node))

(defn node-text
  "Returns a node's text value

  **Usage:**

    user> (node-text #<HtmlAnchor HtmlAnchor[<a class=\"foo\" id=\"bar\" href=\"http://example.com\">]>)
    ;=> \"Search\""
  [^DomNode node]
  (.asText node))

(defn attr-map
  "Returns a clojure map of attributes for a given node

  **Usage:**

    user> (attr-map #<HtmlAnchor HtmlAnchor[<a class=\"foo\" id=\"bar\" href=\"http://example.com\">]>)
    ;=> {:text \"Search\", :href \"http://example.com\", :id \"bar\", :class \"foo\"}

  _See also: yokogiri.core/attrs_"
  [^DomNode node]
  (let [^NamedNodeMap attrs (.getAttributes node)]
    (loop [acc 0, res {}]
      (if (= acc (.getLength attrs))
        (assoc res :text (node-text node))
        (recur (inc acc)
               (let [^DomAttr attr (.item attrs acc)]
                 (assoc res (keyword (.getName attr)) (.getValue attr))))))))

(def ^{:doc "_See also: yokogiri.core/attr-map_"} attrs #'yokogiri.core/attr-map)

;; _TODO: http://htmlunit.sourceforge.net/apidocs/com/gargoylesoftware/htmlunit/html/DomAttr.html_
(defn- dom-attr
  "Returns the HtmlUnit DomAttr objects for a given node

  _See also: yokogiri.core/attr-map_"
  [^DomNode node]
  (let [^NamedNodeMap attrs (.getAttributes node)
        len (.getLength attrs)]
    (map #(.item attrs %) (range 0 len))))

(comment
  (def c (make-client))
  (def p (get-page c "http://www.example.com/"))
  (xpath p "//a")
  (map attrs (css p "p"))
)
