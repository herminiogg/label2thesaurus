package com.herminiogarcia.label2thesaurus

import java.net.{URI, URL}


class Reconciler(maxThreshold: Int, caseSensitive: Boolean) {

  def reconcile(labels: List[String], thesaurus: List[URL], alternativePredicates: Option[String]): List[ReconcilerResult] = {
    thesaurus.flatMap(th => {
      val thesaurusManager = new ThesaurusManager(th, caseSensitive, alternativePredicates)
      labels.flatMap(label => {
        val results = thesaurusManager.lookForLabel(label, maxThreshold)
        results.map(r => new ReconcilerResult(label, r.term, r.termLabel, r.lang, r.confidence))
      })
    })
  }.sortBy(_.confidence)(Ordering[Double].reverse)

}

class ReconcilerResult(val label: String, val term: URI, val termLabel: String, val lang: String, val confidence: Double)
