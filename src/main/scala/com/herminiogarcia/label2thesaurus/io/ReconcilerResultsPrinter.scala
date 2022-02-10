package com.herminiogarcia.label2thesaurus.io

import com.herminiogarcia.label2thesaurus.ReconcilerResult

class ReconcilerResultsPrinter(reconcilerResults: List[ReconcilerResult]) {

  def toSysOut(): Unit = {
    reconcilerResults.foreach(r => {
      println("Label: " + r.label)
      println("Term Label: " + r.termLabel)
      println("Language: " + r.lang)
      println("Term: " + r.term)
      println("Confidence: " + r.confidence + "\n")
    })
  }

  def toCSV(path: String): Unit = {
    val fileHandler = new FileHandler(path)
    val contents = reconcilerResults.map(r => r.label + ";" + r.termLabel + ";" + r.lang + ";" + r.term + ";" + r.confidence).mkString("\n")
    val headers = "label;termLabel;language;term;confidence\n"
    fileHandler.write(headers + contents)
  }

}
