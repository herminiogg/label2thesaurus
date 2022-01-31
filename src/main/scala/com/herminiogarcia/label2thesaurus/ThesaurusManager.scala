package com.herminiogarcia.label2thesaurus

import com.github.vickumar1981.stringdistance.StringDistance.Levenshtein
import org.apache.jena.query.{QueryExecutionFactory, QueryFactory}
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.RDFDataMgr

import java.net.{URI, URL}
import scala.collection.mutable

class ThesaurusManager(thesaurusURL: URL, caseSensitive: Boolean) {

  private def chargeThesaurusAsModel(): Model = {
    RDFDataMgr.loadModel(thesaurusURL.toString)
  }

  private def loadFromResources(filePath: String): String = {
    val file = scala.io.Source.fromResource(filePath)
    val content = file.mkString
    file.close()
    content
  }

  private def calculateLevenshteinDistance(label: String, itemLabel: String): Int = {
    val labelFinalValue = if(!caseSensitive) label.toLowerCase else label
    val itemLabelFinalValue = if(!caseSensitive) itemLabel.toLowerCase else itemLabel
    Levenshtein.distance(labelFinalValue, itemLabelFinalValue)
  }

  def lookForLabel(label: String, maxThreshold: Int): List[ThesaurusLabelLookupResult] = {
    val model = chargeThesaurusAsModel()
    val sparql = loadFromResources("getAllLabels.sparql")
    val query = QueryFactory.create(sparql)
    val queryExecution = QueryExecutionFactory.create(query, model)
    val resultSet = queryExecution.execSelect()
    val results = mutable.ListBuffer[ThesaurusLabelLookupResult]()
    while(resultSet.hasNext) {
      val result = resultSet.next()
      val item = result.getResource("item").getURI
      val itemLabelResource = result.get("itemLabel")
      val itemLabel =
        if(itemLabelResource.isLiteral) itemLabelResource.asLiteral().getString
        else itemLabelResource.asResource().getURI
      val itemLanguage = if(itemLabelResource.isLiteral) itemLabelResource.asLiteral().getLanguage else ""
      val distance = calculateLevenshteinDistance(label, itemLabel)
      if(distance < maxThreshold) {
        val confidence = (maxThreshold - distance) / maxThreshold.toDouble * 100.0
        results += new ThesaurusLabelLookupResult(URI.create(item), itemLabel, itemLanguage, confidence)
      }
    }
    results.toList
  }

}

class ThesaurusLabelLookupResult(val term: URI, val termLabel: String, val lang: String, val confidence: Double)
