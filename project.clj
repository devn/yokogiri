(defproject yokogiri "1.4.0"
  :description "Barebones Nokogiri for Clojure"
  :url "https://github.com/devn/yokogiri"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [net.sourceforge.htmlunit/htmlunit "2.9"]
                 [se.fishtank/css-selectors "1.0.2"]]
  :profiles {:dev {:dependencies [[midje "1.3.1"]]}})
