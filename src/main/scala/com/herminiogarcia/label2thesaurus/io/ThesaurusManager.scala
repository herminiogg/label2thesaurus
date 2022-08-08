package com.herminiogarcia.label2thesaurus.io

import com.herminiogarcia.label2thesaurus.distance.DistanceOrScoreCalculator
import org.apache.jena.query.{Query, QueryExecution, QueryExecutionFactory, QueryFactory}
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.RDFDataMgr

import java.net.{URI, URL}
import scala.collection.mutable

object ThesaurusManagerFactory {

  def apply(thesaurusURL: ThesaurusURL, alternativePredicates: Option[String], distanceCalculator: DistanceOrScoreCalculator): ThesaurusManager = thesaurusURL match {
    case FileURL(url) => ThesaurusManagerViaFile(url, alternativePredicates, distanceCalculator)
    case SPARQLEndpoint(url) => ThesaurusManagerViaSPARQLEndpoint(url, alternativePredicates, distanceCalculator)
  }

}

sealed trait ThesaurusManager {
  def lookForLabel(label: String, maxThreshold: Double): List[ThesaurusLabelLookupResult]

  protected def doSparqlQuery(label: String, maxThreshold: Double, queryExecution: QueryExecution,
                              distanceCalculator: DistanceOrScoreCalculator): List[ThesaurusLabelLookupResult] = {
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

  protected def loadSparqlQuery(alternativePredicates: Option[String]): Query = {
    val sparqlResource = loadFromResources("getAllLabels.sparql")
    val sparql =
      if(alternativePredicates.isDefined) sparqlResource.replace("$predicates", alternativePredicates.get)
      else sparqlResource.replace("$predicates", "skos:prefLabel|skos:altLabel")
    QueryFactory.create(sparql)
  }

  private def loadFromResources(filePath: String): String = {
    val file = scala.io.Source.fromResource(filePath)
    val content = file.mkString
    file.close()
    content
  }
}

case class ThesaurusManagerViaFile(thesaurusURL: URL, alternativePredicates: Option[String], distanceCalculator: DistanceOrScoreCalculator) extends ThesaurusManager {

  private def chargeThesaurusAsModel(): Model = {
    RDFDataMgr.loadModel(thesaurusURL.toString)
  }

  def lookForLabel(label: String, maxThreshold: Double): List[ThesaurusLabelLookupResult] = {
    val model = chargeThesaurusAsModel()
    val query = loadSparqlQuery(alternativePredicates)
    val queryExecution = QueryExecutionFactory.create(query, model)
    doSparqlQuery(label, maxThreshold, queryExecution, distanceCalculator)
  }

}

case class ThesaurusManagerViaSPARQLEndpoint(sparqlEndpoint: URL, alternativePredicates: Option[String], distanceCalculator: DistanceOrScoreCalculator) extends ThesaurusManager {

  def lookForLabel(label: String, maxThreshold: Double): List[ThesaurusLabelLookupResult] = {
    val query = loadSparqlQuery(alternativePredicates)
    val queryExecution = QueryExecutionFactory.sparqlService(sparqlEndpoint.toString, query)
    doSparqlQuery(label, maxThreshold, queryExecution, distanceCalculator)
  }

}

class ThesaurusLabelLookupResult(val term: URI, val termLabel: String, val lang: String, val confidence: Double)
