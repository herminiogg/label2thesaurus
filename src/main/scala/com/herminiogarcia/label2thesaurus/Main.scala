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

  override def call(): Int = {
    val thesauri = new FileHandler(thesauriPath).splitByLine().map(new URL(_))
    val labels = new FileHandler(labelsPath).splitByLine()
    val results = new Reconciler(threshold).reconcile(labels.toList, thesauri.toList)
    val printer = new ReconcilerResultsPrinter(results)
    if(outputPath.isEmpty)
      printer.toSysOut()
    else
      printer.toCSV(outputPath)
    1 // well finished
  }

}
