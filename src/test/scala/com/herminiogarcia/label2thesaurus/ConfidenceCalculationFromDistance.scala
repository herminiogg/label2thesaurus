package com.herminiogarcia.label2thesaurus

import com.herminiogarcia.label2thesaurus.reconciliation.{Reconciler, ReconcilerResult}
import org.scalatest.funsuite.AnyFunSuite

import java.net.{URI, URL}

class ConfidenceCalculationFromDistance extends AnyFunSuite {

  //Levenshtein - case sensitive different thresholds
  test("fotografie: Case sensitive Levenshtein distance confidence calculation, threshold=4") {
    val results = callEHRITermsReconciler(4, true, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","pl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.75)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "fotografika","pl", 0.5)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/793"), "Demografie","de", 0.25)))
  }

  test("fotografie: Case sensitive Levenshtein distance confidence calculation, threshold=2") {
    val results = callEHRITermsReconciler(2, true, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografii","ru-Latn", 0.5)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.5)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","de", 0.5)))
  }

  test("fotografie: Case sensitive Levenshtein distance confidence calculation, threshold=6") {
    val results = callEHRITermsReconciler(6, true, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.8333333333333334)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografia","it", 0.6666666666666666)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/788"), "Storiografia","it", 0.16666666666666666)))
  }

  //Levenshtein - no case sensitive different thresholds
  test("fotografie: No case sensitive Levenshtein distance confidence calculation, threshold=4") {
    val results = callEHRITermsReconciler(4, false, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","pl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografin","de", 0.75)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/793"), "Demografie","de", 0.25)))
  }

  test("fotografie: No case sensitive Levenshtein distance confidence calculation, threshold=2") {
    val results = callEHRITermsReconciler(2, false, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografii","ru-Latn", 0.5)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografia","it", 0.5)))
  }

  test("fotografie: No case sensitive Levenshtein distance confidence calculation, threshold=6") {
    val results = callEHRITermsReconciler(6, false, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografi","it", 0.8333333333333334)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/788"), "Storiografia","it", 0.16666666666666666)))
  }

  //Hamming - case sensitive different thresholds
  test("fotografie: Case sensitive Hamming distance confidence calculation, threshold=4") {
    val results = callEHRITermsReconciler(4, true, "Hamming")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","pl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.75)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografin","de", 0.5)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/793"), "Demografie","de", 0.25)))
  }

  test("fotografie: Case sensitive Hamming distance confidence calculation, threshold=2") {
    val results = callEHRITermsReconciler(2, true, "Hamming")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografii","ru-Latn", 0.5)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.5)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","de", 0.5)))
  }

  test("fotografie: Case sensitive Hamming distance confidence calculation, threshold=6") {
    val results = callEHRITermsReconciler(6, true, "Hamming")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.8333333333333334)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografia","it", 0.6666666666666666)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/231"), "Remigratie","nl", 0.16666666666666666)))
  }

  //LongestCommonSubsequence - no case sensitive different thresholds
  test("fotografie: Case sensitive LongestCommonSubsequence distance confidence calculation, threshold=4") {
    val results = callEHRITermsReconciler(4, true, "LongestCommonSubsequence")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","pl", 0.6)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.5555555555555556)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografin","de", 0.5)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/793"), "Demografie","de", 0.42857142857142855)))
  }

  test("fotografie: Case sensitive LongestCommonSubsequence distance confidence calculation, threshold=2") {
    val results = callEHRITermsReconciler(2, true, "LongestCommonSubsequence")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 0.8)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografii","ru-Latn", 0.7777777777777778)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.7777777777777778)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografia","it", 0.75)))
  }

  test("fotografie: Case sensitive LongestCommonSubsequence distance confidence calculation, threshold=6") {
    val results = callEHRITermsReconciler(6, true, "LongestCommonSubsequence")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 0.4)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.3333333333333333)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografi","it", 0.25)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/788"), "Storiografia","it", 0.14285714285714285)))
  }


  def callEHRITermsReconciler(threshold: Double, caseSensitive: Boolean, algorithm: String): List[ReconcilerResult] = {
    val thesaurus = List(new URL("https://portal.ehri-project.eu/vocabularies/ehri_terms/export?format=RDF%2FXML"))
    val labels = List("fotografie")
    new Reconciler(threshold, caseSensitive, Option(algorithm), false).reconcile(labels, thesaurus, None)
  }

}
