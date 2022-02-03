package com.herminiogarcia.label2thesaurus

import com.herminiogarcia.label2thesaurus.io.{FileHandler, ReconcilerResultsPrinter}
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
  description = Array("Links your keywords to existing thesaurus terms based on similarity"))
class Main extends Callable[Int] {

  @Option(names = Array("-t", "--thesauri"), required = true, description = Array("Path to the file with the list of thesauri"))
  private var thesauriPath: String = ""

  @Option(names = Array("-l", "--labels"), required = true, description = Array("Path to the file with the strings to be reconciled"))
  private var labelsPath: String = ""

  @Option(names = Array("-th", "--threshold"), description = Array("Maximum threshold to use for the string distance"))
  private var threshold: Int = 10

  @Option(names = Array("-o", "--output"), description = Array("Path where to generate the output files"))
  private var outputPath: String = ""

  @Option(names = Array("-cs", "--casesensitive"), description = Array("Use case sensitive comparison"))
  private var caseSensitive: Boolean = false

  @Option(names = Array("-p", "--predicates"), description = Array("Alternative predicates to look for the labels in the generated KG. Syntax example and default: skos:prefLabel|skos:altLabel." +
    "Take into account that only rdfs, skos, dcterms, dc and foaf namespaces are included, so to include another namespace predicate you must provide it with the full IRI syntax, e.g.: <http://xmlns.com/foaf/0.1/>"))
  private var alternativePredicates: String = ""

  @Option(names = Array("-d", "--distance"), description = Array("Algorithm to use for the distance calculation. Available: Levenshtein, Damerau-Levenshtein, Hamming, LongestCommonSubsequence. Default: Levenshtein "))
  private var distanceCalculation: String = ""


  override def call(): Int = {
    val thesauri = new FileHandler(thesauriPath).splitByLine().map(new URL(_))
    val labels = new FileHandler(labelsPath).splitByLine()
    val alternativePredicatesOption = if(alternativePredicates.isEmpty) None else scala.Option(alternativePredicates)
    val distanceAlgorithm = if(distanceCalculation.isEmpty) None else scala.Option(distanceCalculation)
    val results = new Reconciler(threshold, caseSensitive, distanceAlgorithm).reconcile(labels.toList, thesauri.toList, alternativePredicatesOption)
    val printer = new ReconcilerResultsPrinter(results)
    if(outputPath.isEmpty)
      printer.toSysOut()
    else
      printer.toCSV(outputPath)
    1 // well finished
  }

}
