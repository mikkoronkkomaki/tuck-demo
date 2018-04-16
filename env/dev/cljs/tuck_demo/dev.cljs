(ns ^:figwheel-no-load tuck-demo.dev
  (:require
    [tuck-demo.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
