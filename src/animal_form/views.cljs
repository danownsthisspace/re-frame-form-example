(ns animal-form.views
  (:require
   [re-frame.core :as re-frame]
   [animal-form.events :as events]
   [animal-form.subs :as subs]))

(def animal-types ["Dog" "Cat" "Mouse"])

(defn animal-list []
  (let [animals @(re-frame/subscribe [::subs/animals])]
    [:div
     [:h1 "animal list"]
     [:ul
      (map (fn [{:keys [animal-type animal-name]}]
             [:li {:key animal-name} (str animal-name " (" animal-type ")")]) animals)]]))

(defn text-input [id label]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:input.input {:value @value
                     :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])
                     :type "text" :placeholder "Text input"}]]]))

(defn select-input [id label options]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:div.select
       [:select {:value @value :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])}
        [:option {:value ""} "Please select"]
        (map (fn [o] [:option {:key o :value o} o]) options)]]]]))

(defn main-panel []
  (let [is-valid? @(re-frame/subscribe [::subs/form-is-valid? [:animal-name :animal-type]])]
    [:div.section
     [animal-list]
     [text-input :animal-name "Animal Name"]
     [select-input :animal-type "Animal Type" animal-types]
     [:button.button.is-primary {:disabled (not is-valid?)
                                 :on-click #(re-frame/dispatch [::events/save-form])} "Save"]]))
