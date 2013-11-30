(defproject yokogiri "1.5.4"
  :description "Barebones Nokogiri for Clojure"
  :license {:name "Eclipse Public License - Version 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :comments "Same as Clojure"
            :distribution :repo}
  :url "https://github.com/devn/yokogiri"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [net.sourceforge.htmlunit/htmlunit "2.12"]
                 [se.fishtank/css-selectors "1.0.4"]]
  :profiles {:dev {:dependencies [[midje "1.5.1" :exclusions [org.clojure/clojure]]]
                   :plugins [[lein-midje "3.0.1"]]}}
  :min-lein-version "2.0.0")
