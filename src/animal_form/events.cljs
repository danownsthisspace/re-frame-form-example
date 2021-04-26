(ns animal-form.events
  (:require
   [re-frame.core :as re-frame]
   [animal-form.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-db
 ::update-form
 (fn [db [_ id val]]
   (assoc-in db [:form id] val)))

(re-frame/reg-event-db
 ::save-form
 (fn [db]
   (let [form-data (:form db)
         animals (get db :animals [])
         updated-animals (conj animals form-data)]
     (-> db
         (assoc :animals updated-animals)
         (dissoc :form)))))