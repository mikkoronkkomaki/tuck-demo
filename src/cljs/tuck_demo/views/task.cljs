(ns tuck-demo.views.task
  (:require [tuck.core :refer [tuck]]
            [tuck-demo.data.task :as tasks-data]
            [reagent.core :refer [atom]]
            [cljs.core.async :refer [chan <!]]))

(defn single-task [e! id {:keys [description done?]}]
  [:li
   [:span
    {:onClick #(e! (tasks-data/->ToggleTaskCompletion id))}
    (if done?
      [:strike description]
      description)]
   " "
   [:button
    {:type "button"
     :onClick #(e! (tasks-data/->DeleteTask id))}
    "x"]])

(defn tasks-list [e! {:keys [tasks]}]
  (into [:ul] (mapv
                #(single-task e! % (get tasks %))
                (keys tasks))))

(defn new-task-form [e!]
  (let [new-task (atom nil)]
    (fn []
      [:form
       [:input
        {:type "text"
         :placeholder "New task"
         :value @new-task
         :on-change #(reset! new-task (-> % .-target .-value))}]
       [:input
        {:type "submit"
         :value "Add"
         :onClick #(let [value @new-task]
                     (.preventDefault %)
                     (reset! new-task nil)
                     (e! (tasks-data/->NewTask value)))}]])))

(defn tasks* [e! app]
  (fn [e! app]
    [:div
     [:h3 "New task"]
     [new-task-form e!]

     [:h3 "Tasks"]
     [tasks-list e! app]]))

(defn task-view []
  [tuck tasks-data/tasks-app tasks*])