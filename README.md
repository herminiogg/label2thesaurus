# label2thesaurus
This is a library meant to help with the identification and reconciliation of controlled
vocabulary terms from free text labels based on string similarity algorithms. It returns
the matches with a confidence interval which supports a semi-supervised entity linking
workflow.

# How to start
In order to run a simple example you should provide two different fields: a list of thesuari in form
of a URL pointing to each of them and a list of terms to be reconciled (place one term or URL per line).
For example, you can use the labels.txt and thesauri.txt files, that are already provided with the project, using
the following command:

`$ java -jar label2thesaurus.jar -t thesauri.txt -l labels.txt -o coreference.csv -d Levenshtein`

You will see the result of the execution under the `coreference.tsv` file.

## Options
This library offer different options for configuring the algorithms to run and the specific library behaviour.

### Distance and score calculation
This library strongly relies in the StringDistance Scala library (https://github.com/vickumar1981/stringdistance) for the
distance similarity calculation. Therefore, the algorithms supported are very related to those supported in the mentioned
library. 

* Distance
  * Levenshtein
  * Damerau-Levenshtein
  * Hamming
  * LongestCommonSubsequence
* Score
  * Cosine
  * Damerau-Levenshtein
  * Dice
  * Hamming
  * Jaro
  * Levenshtein
  * Metaphone
  * Soundex

### Threshold
In order to avoid very long outputs that would be impossible to be supervised by a human, this library offers the possibility
to configure a threshold. This threshold operates using a different criteria depending on the used algorithm due to the 
different meaning of the outputs.

For distance calculation (excluding LongestCommonSubsequence) the threshold mean the maximum value that a distance result should
have in order to be returned to the user. In addition, the confidence percentage is calculated as: `(threshold - distance) / threshold`
In the case of the LongestCommonSubsequence the distance meaning is the inverse as a higher value means greater similarity. Therefore, for this
algorithm the threshold is the minimum value that a result should return in order to be included and the confidence percentage is
calculated as: `(distance - minThreshold) / distance`

Score algorithms already give a percentage of confidence so this is taken directly from the algorithm. So, for these
algorithms the threshold only acts as a value to filter out the values that are under the given limit.
Take into account that Phonetic similarity algorithms only return true or false, so in these two algorithms the output will be
100% or 0%, with the threshold acting in the same behaviour as described for the rest of the score ones.

### Custom predicates
By default, the algorithm uses the values under the predicates `skos:prefLabel` and `skos:altLabel` for the string similarity
calculation. However, it is possible to use other vocabularies and predicates (see CLI instructions below). Only some limited
namespaces are included (i.e., rdfs, skos, dcterms, dc and foaf), so to use another one the predicate should be introduced
using the full IRI syntax. For example, for schema:name we should use: `<https://schema.org/name>`

# CLI
```
Usage: label2thesaurus [-hV] [-cs] [-d=<distanceCalculation>] -l=<labelsPath>
                       [-o=<outputPath>] [-p=<alternativePredicates>]
                       [-s=<scoreCalculation>] -t=<thesauriPath>
                       [-th=<threshold>]
Links your keywords to existing thesaurus terms based on similarity
      -cs, --casesensitive   Use case sensitive comparison
  -d, --distance=<distanceCalculation>
                             Algorithm to use for the distance calculation.
                               Available: Levenshtein, Damerau-Levenshtein,
                               Hamming, LongestCommonSubsequence. Default:
                               Levenshtein
  -h, --help                 Show this help message and exit.
  -l, --labels=<labelsPath>  Path to the file with the strings to be reconciled
  -o, --output=<outputPath>  Path where to generate the output files
  -p, --predicates=<alternativePredicates>
                             Alternative predicates to look for the labels in
                               the generated KG. Syntax example and default:
                               skos:prefLabel|skos:altLabel.Take into account
                               that only rdfs, skos, dcterms, dc and foaf
                               namespaces are included, so to include another
                               namespace predicate you must provide it with the
                               full IRI syntax, e.g.: <http://xmlns.com/foaf/0.
                               1/>
  -s, --score=<scoreCalculation>
                             Algorithm to use for the score calculation, if you
                               use score calculation then the distance and
                               threshold will not be used. Available: Cosine,
                               Damerau-Levenshtein, Dice, Hamming, Jaro,
                               Levenshtein, Metaphone and Soundex
  -t, --thesauri=<thesauriPath>
                             Path to the file with the list of thesauri
      -th, --threshold=<threshold>
                             Maximum threshold to use for the string distance
                               (e.g., 5) or min confidence for the score (e.g.,
                               0.5)
  -V, --version              Print version information and exit.
```

# JVM API
As this library is written in Scala it offers a JVM compatible interface. You can use it by instantiating
the Reconciler class and calling the method reconcile (see the example below).

```scala
val thesaurus = List(new URL("https://portal.ehri-project.eu/vocabularies/ehri_terms/export?format=RDF%2FXML"))
val labels = List("fotografie")
new Reconciler(threshold, caseSensitive, Option(algorithm), true).reconcile(labels, thesaurus, None)
```

