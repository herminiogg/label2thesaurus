package com.herminiogarcia.label2thesaurus

import com.herminiogarcia.label2thesaurus.reconciliation.{Reconciler, ReconcilerResult}

import java.net.URL

trait ReconcilerCaller {

  def callEHRITermsReconciler(threshold: Double, caseSensitive: Boolean, algorithm: String): List[ReconcilerResult] = {
    val thesaurus = List(new URL("https://portal.ehri-project.eu/vocabularies/ehri_terms/export?format=RDF%2FXML"))
    val labels = List("fotografie")
    new Reconciler(threshold, caseSensitive, Option(algorithm), false).reconcile(labels, thesaurus, None)
  }

  def callEHRITermsScoreReconciler(threshold: Double, caseSensitive: Boolean, algorithm: String): List[ReconcilerResult] = {
    val thesaurus = List(new URL("https://portal.ehri-project.eu/vocabularies/ehri_terms/export?format=RDF%2FXML"))
    val labels = List("fotografie")
    new Reconciler(threshold, caseSensitive, Option(algorithm), true).reconcile(labels, thesaurus, None)
  }

}
