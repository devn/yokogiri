(defproject yokogiri "1.5.1"
  :description "Barebones Nokogiri for Clojure"
  :license {:name "Eclipse Public License - Version 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :comments "Same as Clojure"
            :distribution :repo}
  :url "https://github.com/devn/yokogiri"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [net.sourceforge.htmlunit/htmlunit "2.9"]
                 [se.fishtank/css-selectors "1.0.2"]]
  :profiles {:dev {:dependencies [[midje "1.3.1"]]}}
  :min-lein-version "2.0.0")
