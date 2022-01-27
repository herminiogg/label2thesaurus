package com.herminiogarcia.label2thesaurus

import java.net.URL

object Main {

  def main(args: Array[String]): Unit = {
    val result = new Reconciler(10).reconcile(List("fotografie", "deportaties"), List(new URL("https://portal.ehri-project.eu/vocabularies/ehri_terms/export?format=RDF%2FXML")))
    result.foreach(r => {
      println("Label: " + r.label)
      println("Term Label: " + r.termLabel)
      println("Language: " + r.lang)
      println("Term: " + r.term)
      println("Confidence: " + r.confidence + "\n")
    })
  }

}
