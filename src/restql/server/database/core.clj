(ns restql.server.database.core
  (:require [restql.server.database.persistence :as db]
            [environ.core :refer [env]]
            [clojure.edn :as edn]
            [restql.core.validator.core :as validator]
            [slingshot.slingshot :refer [throw+]]
            [org.httpkit.client :as http]))

;re-exporting find-query
(def find-query-by-id-and-revision db/find-query)
(def count-query-revisions db/count-query-revisions)
(def find-all-queries-by-namespace db/find-all-queries-by-namespace)

(defn save-query [query-ns id query]
  (let [parsed-query (->> query
                          :text
                          edn/read-string)]
    (if (validator/validate {:mappings env} parsed-query)
      (db/save-query query-ns id query)
      (throw+ {:type :pdg-query-validation-error :data query}))))
