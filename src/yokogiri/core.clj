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
  '{:activex-native                  #(.setActiveXNative %1 %2)
    :applet                          #(.setAppletEnabled %1 %2)
    :block-popups                    #(.setPopupBlockerEnabled %1 %2)
    :css                             #(.setCssEnabled %1 %2)
    :geolocation                     #(.setGeolocationEnabled %1 %2)
    :homepage                        #(.setHomePage %1 %2)
    :insecure-ssl                    #(.setUseInsecureSSL %1 %2)
    :print-content-on-failing-status #(.setPrintContentOnFailingStatusCode %1 %2)
    :redirects                       #(.setRedirectEnabled %1 %2)
    :throw-on-failing-status         #(.setThrowExceptionOnFailingStatusCode %1 %2)
    :throw-on-script-error           #(.setThrowExceptionOnScriptError %1 %2)
    :timeout                         #(.setTimeout %1 %2)
    :tracking                        #(.setDoNotTrackEnabled %1 %2)
    :javascript                      #(.setJavaScriptEnabled %1 %2)})

(defn set-client-options!
  "Sets options on the client. See
  yokogiri.core/set-client-options-map for available options."
  [^WebClient client opts]
  (let [^WebClientOptions client-opts (web-client-options client)]
    (doseq [[k v] opts]
      (let [setter-fn (get set-client-options-map k)]
        (setter-fn client-opts v)))
    client))

(defn get-client-options
  "Returns a map of all options currently set on a client.

  Usage:
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

  Basic Example: (make-client)
  With Options: (make-client :javascript true :redirects true)"
  [& {:as opts}]
  (let [client (new WebClient)]
    (if-not (empty? opts)
      (set-client-options! (new WebClient) opts)
      client)))

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
  ;=> (#<HtmlAnchor HtmlAnchor[<a onclick...>]>, ...)"
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
  [^DomNode node]
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
