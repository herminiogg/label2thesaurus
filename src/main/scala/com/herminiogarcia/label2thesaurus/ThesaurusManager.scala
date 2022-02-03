package com.herminiogarcia.label2thesaurus

import com.herminiogarcia.label2thesaurus.distance.DistanceCalculator
import org.apache.jena.query.{QueryExecutionFactory, QueryFactory}
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.RDFDataMgr

import java.net.{URI, URL}
import scala.collection.mutable

class ThesaurusManager(thesaurusURL: URL, alternativePredicates: Option[String], distanceCalculator: DistanceCalculator) {

  private def chargeThesaurusAsModel(): Model = {
    RDFDataMgr.loadModel(thesaurusURL.toString)
  }

  private def loadFromResources(filePath: String): String = {
    val file = scala.io.Source.fromResource(filePath)
    val content = file.mkString
    file.close()
    content
  }

  def lookForLabel(label: String, maxThreshold: Int): List[ThesaurusLabelLookupResult] = {
    val model = chargeThesaurusAsModel()
    val sparqlResource = loadFromResources("getAllLabels.sparql")
    val sparql =
      if(alternativePredicates.isDefined) sparqlResource.replace("$predicates", alternativePredicates.get)
      else sparqlResource.replace("$predicates", "skos:prefLabel|skos:altLabel")
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
      if(distanceCalculator.toBeFiltered(label, itemLabel, maxThreshold)) {
        val confidence = distanceCalculator.calculateConfidence(label, itemLabel, maxThreshold)
        results += new ThesaurusLabelLookupResult(URI.create(item), itemLabel, itemLanguage, confidence)
      }
    }
    results.toList
  }

}

class ThesaurusLabelLookupResult(val term: URI, val termLabel: String, val lang: String, val confidence: Double)
