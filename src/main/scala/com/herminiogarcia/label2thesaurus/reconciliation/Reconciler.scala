package com.herminiogarcia.label2thesaurus.reconciliation

import com.herminiogarcia.label2thesaurus.distance.{DistanceCalculatorFactory, ScoreCalculatorFactory}
import com.herminiogarcia.label2thesaurus.io.ThesaurusManager

import java.net.{URI, URL}


class Reconciler(maxThreshold: Double, caseSensitive: Boolean, distanceOrScoreAlgorithm: Option[String], isScore: Boolean) {

  def reconcile(labels: List[String], thesaurus: List[URL], alternativePredicates: Option[String]): List[ReconcilerResult] = {
    thesaurus.flatMap(th => {
      val distanceorScoreCalculator =
        if(!isScore) DistanceCalculatorFactory(distanceOrScoreAlgorithm, caseSensitive)
        else ScoreCalculatorFactory(distanceOrScoreAlgorithm, caseSensitive)
      val thesaurusManager = new ThesaurusManager(th, alternativePredicates, distanceorScoreCalculator)
      labels.flatMap(label => {
        val results = thesaurusManager.lookForLabel(label, maxThreshold)
        results.map(r => new ReconcilerResult(label, r.term, r.termLabel, r.lang, r.confidence))
      })
    })
  }.sortBy(_.confidence)(Ordering[Double].reverse)

}

class ReconcilerResult(val label: String, val term: URI, val termLabel: String, val lang: String, val confidence: Double)