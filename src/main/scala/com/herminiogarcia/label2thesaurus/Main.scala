package com.herminiogarcia.label2thesaurus

import com.herminiogarcia.label2thesaurus.io.{FileHandler, ReconcilerResultsPrinter}
import com.herminiogarcia.label2thesaurus.reconciliation.Reconciler
import picocli.CommandLine
import picocli.CommandLine.{Command, Option}

import java.net.URL
import java.util.concurrent.Callable

object Main {

  def main(args: Array[String]): Unit = {
    System.exit(new CommandLine(new Main()).execute(args: _*))
  }
}

@Command(name = "label2thesaurus", version = Array("v0.1.0"),
  mixinStandardHelpOptions = true,
  description = Array("Links your keywords to existing thesaurus terms based on string similarity"))
class Main extends Callable[Int] {

  @Option(names = Array("-t", "--thesauri"), required = false, description = Array("Path to the file with the list of thesauri"))
  private var thesauriPath: String = ""

  @Option(names = Array("-se", "--sparqlEndpoints"), required = false, description = Array("Path to the file with the list of SPARQL endpoints"))
  private var sparqlEndpointsPath: String = ""

  @Option(names = Array("-l", "--labels"), required = true, description = Array("Path to the file with the strings to be reconciled"))
  private var labelsPath: String = ""

  @Option(names = Array("-th", "--threshold"), description = Array("Maximum threshold to use for the string distance (e.g., 5) or min confidence for the score (e.g., 0.5)"))
  private var threshold: Double = Double.NaN

  @Option(names = Array("-o", "--output"), description = Array("Path where to generate the output files"))
  private var outputPath: String = ""

  @Option(names = Array("-cs", "--casesensitive"), description = Array("Use case sensitive comparison"))
  private var caseSensitive: Boolean = false

  @Option(names = Array("-p", "--predicates"), description = Array("Alternative predicates to look for the labels in the generated KG. Syntax example and default: skos:prefLabel|skos:altLabel." +
    "Take into account that only rdfs, skos, dcterms, dc and foaf namespaces are included, so to include another namespace predicate you must provide it with the full IRI syntax, e.g.: <http://xmlns.com/foaf/0.1/>"))
  private var alternativePredicates: String = ""

  @Option(names = Array("--sparql"), description = Array("Path to a file with a custom SPARQL query"))
  private var alternativeSparqlQueryPath: String = ""

  @Option(names = Array("-d", "--distance"), description = Array("Algorithm to use for the distance calculation. Available: Levenshtein, Damerau-Levenshtein, Hamming, LongestCommonSubsequence. Default: Levenshtein "))
  private var distanceCalculation: String = ""

  @Option(names = Array("-s", "--score"), description = Array("Algorithm to use for the score calculation, if you use score calculation then the distance and threshold will not be used. Available: Cosine, Damerau-Levenshtein, Dice, Hamming, Jaro, Levenshtein, Metaphone and Soundex"))
  private var scoreCalculation: String = ""


  override def call(): Int = {
    if(thesauriPath.isEmpty && sparqlEndpointsPath.isEmpty) {
      System.err.println("Please, provide a file with thesauri URIs or a list of SPARQL endpoints: -t and/or -se")
      -1
    } else {
      val thesauri = if(thesauriPath.isEmpty) List() else new FileHandler(thesauriPath).splitByLine().map(new URL(_))
      val sparqlEndpoints = if(sparqlEndpointsPath.isEmpty) List() else new FileHandler(sparqlEndpointsPath).splitByLine().map(new URL(_))
      val labels = new FileHandler(labelsPath).splitByLine()
      val alternativePredicatesOption = if(alternativePredicates.isEmpty) None else scala.Option(alternativePredicates)
      val alternativeSparqlOption = if(alternativeSparqlQueryPath.isEmpty) None
        else scala.Option(new FileHandler(alternativeSparqlQueryPath).getContent())
      val distanceOrScoreAlgorithm = if(scoreCalculation.isEmpty) {
        if(distanceCalculation.isEmpty) None else scala.Option(distanceCalculation)
      } else scala.Option(scoreCalculation)
      val isScore = scoreCalculation.nonEmpty
      val finalThreshold = if(threshold == Double.NaN) {
        if(isScore) 0.5 else 5
      } else threshold
      val results = new Reconciler(finalThreshold, caseSensitive, distanceOrScoreAlgorithm, isScore).reconcile(labels, thesauri, sparqlEndpoints, alternativePredicatesOption, alternativeSparqlOption)
      val printer = new ReconcilerResultsPrinter(results)
      if(outputPath.isEmpty)
        printer.toSysOut()
      else
        printer.toCSV(outputPath)
      1 // well finished
    }
  }

}
