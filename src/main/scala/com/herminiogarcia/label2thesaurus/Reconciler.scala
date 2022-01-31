package com.herminiogarcia.label2thesaurus

import java.net.{URI, URL}


class Reconciler(maxThreshold: Int, caseSensitive: Boolean) {

  def reconcile(labels: List[String], thesaurus: List[URL]): List[ReconcilerResult] = {
    labels.flatMap(label => {
      thesaurus.flatMap(th => {
        val results = new ThesaurusManager(th, caseSensitive).lookForLabel(label, maxThreshold)
        results.map(r => new ReconcilerResult(label, r.term, r.termLabel, r.lang, r.confidence))
      })
    })
  }.sortBy(_.confidence)(Ordering[Double].reverse)

}

class ReconcilerResult(val label: String, val term: URI, val termLabel: String, val lang: String, val confidence: Double)
