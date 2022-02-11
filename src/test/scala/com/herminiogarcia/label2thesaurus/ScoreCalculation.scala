package com.herminiogarcia.label2thesaurus

import com.herminiogarcia.label2thesaurus.reconciliation.ReconcilerResult
import org.scalatest.funsuite.AnyFunSuite

import java.net.URI

class ScoreCalculation extends AnyFunSuite with ReconcilerCaller {

  //Levenshtein - case sensitive different thresholds
  test("fotografie: Case sensitive Levenshtein score calculation, threshold=0.6") {
    val results = callEHRITermsScoreReconciler(0.6, true, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","pl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.9)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "fotografika","pl", 0.8181818181818182)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/793"), "Demografie","de", 0.7)))
  }

  test("fotografie: Case sensitive Levenshtein score calculation, threshold=0.8") {
    val results = callEHRITermsScoreReconciler(0.8, true, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografii","ru-Latn", 0.9)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.9)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","de", 0.9)))
  }

  test("fotografie: Case sensitive Levenshtein score calculation, threshold=0.3") {
    val results = callEHRITermsScoreReconciler(0.3, true, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.9)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografia","it", 0.8)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/788"), "Storiografia","it", 0.5833333333333334)))
  }

  //Levenshtein - no case sensitive different thresholds
  test("fotografie: No case sensitive Levenshtein score calculation, threshold=0.6") {
    val results = callEHRITermsScoreReconciler(0.6, false, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","pl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografin","de", 0.9)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/793"), "Demografie","de", 0.7)))
  }

  test("fotografie: No case sensitive Levenshtein score calculation, threshold=0.8") {
    val results = callEHRITermsScoreReconciler(0.8, false, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografii","ru-Latn", 0.9)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografia","it", 0.9)))
  }

  test("fotografie: No case sensitive Levenshtein score calculation, threshold=0.3") {
    val results = callEHRITermsScoreReconciler(0.3, false, "Levenshtein")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografi","it", 0.9)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/788"), "Storiografia","it", 0.5833333333333334)))
  }

  //Cosine - case sensitive different thresholds
  test("fotografie: Case sensitive Cosine score calculation, threshold=0.6") {
    val results = callEHRITermsScoreReconciler(0.6, true, "Cosine")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","pl", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.9258200997725514)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografin","de", 0.8486684247915055)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/793"), "Demografie","de", 0.7715167498104595)))
  }

  test("fotografie: Case sensitive Cosine score calculation, threshold=0.8") {
    val results = callEHRITermsScoreReconciler(0.8, true, "Cosine")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografii","ru-Latn", 0.9354143466934853)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.9258200997725514)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","de", 0.9258200997725514)))
  }

  test("fotografie: Case sensitive Cosine score calculation, threshold=0.3") {
    val results = callEHRITermsScoreReconciler(0.3, true, "Cosine")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1.0)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.9258200997725514)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografia","it", 0.8571428571428571)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/231"), "Remigratie","nl", 0.5714285714285714)))
  }

  //Dice - case sensitive different thresholds
  test("fotografie: Case sensitive Dice score calculation, threshold=0.6") {
    val results = callEHRITermsScoreReconciler(0.6, true, "Dice")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","pl", 1)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.8888888888888888)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografin","de", 0.7777777777777778)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/793"), "Demografie","de", 0.6666666666666666)))
  }

  test("fotografie: Case sensitive Dice score calculation, threshold=0.8") {
    val results = callEHRITermsScoreReconciler(0.8, true, "Dice")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografii","ru-Latn", 0.8888888888888888)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.8888888888888888)))
    assert(!results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografia","it", 0.75)))
  }

  test("fotografie: Case sensitive Dice score calculation, threshold=0.3") {
    val results = callEHRITermsScoreReconciler(0.3, true, "Dice")
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/701"), "fotografie","cs", 1)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/700"), "Fotografie","nl", 0.8888888888888888)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/797"), "Fotografi","it", 0.8235294117647058)))
    assert(results.contains(new ReconcilerResult("fotografie",
      new URI("http://data.ehri-project.eu/vocabularies/ehri-terms/788"), "Storiografia","it", 0.6)))
  }

}
