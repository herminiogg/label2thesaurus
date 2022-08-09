package com.herminiogarcia.label2thesaurus

import com.herminiogarcia.label2thesaurus.reconciliation.{Reconciler, ReconcilerResult}
import org.scalatest.funsuite.AnyFunSuite

import java.net.{URI, URL}

class CalculationFromSPARQLEndpointAndCustomSPARQLQuery extends AnyFunSuite with ReconcilerCaller {

  val sparqlQuery =
    """
      |PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
      |PREFIX wd: <http://www.wikidata.org/entity/>
      |PREFIX wdt: <http://www.wikidata.org/prop/direct/>
      |
      |SELECT ?item ?itemLabel
      |WHERE
      |{
      |  ?item wdt:P31 wd:Q3624078 ;
      |        wdt:P361 wd:Q458 ;
      |        rdfs:label ?itemLabel .
      |}
      |""".stripMargin

  test("spanien against wikidata") {
    val sparqlEndpoints = List(new URL("https://query.wikidata.org/sparql"))
    val labels = List("spanien")
    val results =
      new Reconciler(4, false, Option("Levenshtein"), false)
        .reconcile(labels, List(), sparqlEndpoints, None, Option(sparqlQuery))

    assert(results.contains(new ReconcilerResult("spanien",
      new URI("http://www.wikidata.org/entity/Q29"), "Spanien","de", 1.0)))
  }

  test("belgique against wikidata") {
    val sparqlEndpoints = List(new URL("https://query.wikidata.org/sparql"))
    val labels = List("belgique")
    val results =
      new Reconciler(0.4, false, Option("Damerau-Levenshtein"), true)
        .reconcile(labels, List(), sparqlEndpoints, None, Option(sparqlQuery))

    assert(results.contains(new ReconcilerResult("belgique",
      new URI("http://www.wikidata.org/entity/Q31"), "Belgique","fr", 1.0)))
    assert(results.contains(new ReconcilerResult("belgique",
      new URI("http://www.wikidata.org/entity/Q31"), "België","nl", 0.625)))
    assert(results.contains(new ReconcilerResult("belgique",
      new URI("http://www.wikidata.org/entity/Q31"), "Belgium","en", 0.75)))
  }

}
