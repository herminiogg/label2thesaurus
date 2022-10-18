package com.herminiogarcia.label2thesaurus

import com.herminiogarcia.label2thesaurus.reconciliation.{Reconciler, ReconcilerResult}

import java.net.URL

trait ReconcilerCaller {

  def callEHRITermsReconciler(threshold: Double, caseSensitive: Boolean, algorithm: String): List[ReconcilerResult] = {
    val thesaurus = List(new URL("https://portal.ehri-project.eu/vocabularies/ehri_terms/export?format=RDF%2FXML"))
    val labels = List("fotografie")
    new Reconciler(threshold, caseSensitive, Option(algorithm), false).reconcile(labels, thesaurus, List(), None, None)
  }

  def callEHRITermsScoreReconciler(threshold: Double, caseSensitive: Boolean, algorithm: String): List[ReconcilerResult] = {
    val thesaurus = List(new URL("https://portal.ehri-project.eu/vocabularies/ehri_terms/export?format=RDF%2FXML"))
    val labels = List("fotografie")
    new Reconciler(threshold, caseSensitive, Option(algorithm), true).reconcile(labels, thesaurus, List(), None, None)
  }

  def callEHRITermsReconcilerAgainstSPARQLEndpoint(threshold: Double, caseSensitive: Boolean, algorithm: String): List[ReconcilerResult] = {
    val sparqlEndpoints = List(new URL("http://localhost:3030/example/sparql"))
    val labels = List("fotografie")
    new Reconciler(threshold, caseSensitive, Option(algorithm), false).reconcile(labels, List(), sparqlEndpoints, None, None)
  }

  def callEHRITermsScoreReconcilerAgainstSPARQLEndpoint(threshold: Double, caseSensitive: Boolean, algorithm: String): List[ReconcilerResult] = {
    val sparqlEndpoints = List(new URL("http://localhost:3030/example/sparql"))
    val labels = List("fotografie")
    new Reconciler(threshold, caseSensitive, Option(algorithm), true).reconcile(labels, List(), sparqlEndpoints, None, None)
  }

}
