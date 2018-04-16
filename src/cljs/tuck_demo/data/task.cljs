(ns tuck-demo.data.task
  (:require [tuck.core :refer [tuck wrap wrap-path send-value! Event process-event
                               action!
                               send-async!]
             :refer-macros [define-event]]
            [reagent.core :refer [atom]]))

(defonce tasks-app
         (atom {:tasks {1 {:description "first"
                           :done? false}
                        2 {:description "second"
                           :done? false}
                        3 {:description "third"
                           :done? false}}}))

(defrecord ToggleTaskCompletion [id])
(defrecord NewTask [description])
(defrecord DeleteTask [id])

(extend-protocol Event
  ToggleTaskCompletion
  (process-event [{id :id} {tasks :tasks :as app}]
    (assoc app :tasks (update-in tasks [id :done?] not)))

  NewTask
  (process-event [{description :description} {tasks :tasks :as app}]
    (let [id (inc (max (keys tasks)))]
      (assoc app :tasks (assoc
                          tasks
                          id
                          {:description description
                           :done? false}))))
  
  DeleteTask
  (process-event [{id :id} {tasks :tasks :as app}]
    (assoc app :tasks (dissoc tasks id))))