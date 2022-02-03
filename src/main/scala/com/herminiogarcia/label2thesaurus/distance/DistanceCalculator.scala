package com.herminiogarcia.label2thesaurus.distance

import com.github.vickumar1981.stringdistance.StringDistance.{Damerau, Hamming, Levenshtein, LongestCommonSeq}

sealed trait DistanceCalculator {
  val caseSensitive: Boolean

  def distance(label: String, thesaurusLabel: String): Option[Int]
  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Int): Double
  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Int): Boolean
  protected def caseSensitive(label: String): String = {
    if(!caseSensitive) label.toLowerCase else label
  }
  protected def applyDistanceFunction(label: String, thesaurusLabel: String, distanceFunction: (String, String) => Int): Int = {
    distanceFunction(caseSensitive(label), caseSensitive(thesaurusLabel))
  }
}

trait NormalConfidenceCalculator {
  protected def doCalculateConfidence(distance: Int, maxThreshold: Int): Double = {
    (maxThreshold - distance) / maxThreshold.toDouble * 100.0
  }

  protected def doToBeFiltered(distance: Int,  maxThreshold: Int): Boolean = distance < maxThreshold
}

trait InverseConfidenceCalculator {
  protected def doCalculateConfidence(distance: Int, minThreshold: Int): Double = {
    val confidence = (distance - minThreshold) / minThreshold.toDouble * 100.0
    if(distance < minThreshold) 0 else confidence
  }

  protected def doToBeFiltered(distance: Int,  maxThreshold: Int): Boolean = distance > maxThreshold
}

case class LevenshteinDistance(caseSensitive: Boolean) extends DistanceCalculator with NormalConfidenceCalculator {

  def distance(label: String, thesaurusLabel: String): Option[Int] = {
    Some(applyDistanceFunction(label, thesaurusLabel, Levenshtein.distance))
  }

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Int): Double = {
    doCalculateConfidence(distance(label, thesaurusLabel).getOrElse(maxThreshold), maxThreshold)
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Int): Boolean = {
    doToBeFiltered(distance(label, thesaurusLabel).getOrElse(maxThreshold), maxThreshold)
  }
}

case class DamerauLevenshteinDistance(caseSensitive: Boolean) extends DistanceCalculator with NormalConfidenceCalculator {
  def distance(label: String, thesaurusLabel: String): Option[Int] = {
    Some(applyDistanceFunction(label, thesaurusLabel, Damerau.distance))
  }

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Int): Double = {
    doCalculateConfidence(distance(label, thesaurusLabel).getOrElse(maxThreshold), maxThreshold)
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Int): Boolean = {
    doToBeFiltered(distance(label, thesaurusLabel).getOrElse(maxThreshold), maxThreshold)
  }
}

case class HammingDistance(caseSensitive: Boolean) extends DistanceCalculator with NormalConfidenceCalculator {
  def distance(label: String, thesaurusLabel: String): Option[Int] = {
    val distance = applyDistanceFunction(label, thesaurusLabel, Hamming.distance)
    if(distance < 0) None else Some(distance)
  }

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Int): Double = {
    doCalculateConfidence(distance(label, thesaurusLabel).getOrElse(maxThreshold), maxThreshold)
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Int): Boolean = {
    doToBeFiltered(distance(label, thesaurusLabel).getOrElse(maxThreshold), maxThreshold)
  }
}

case class LongestCommonSubsequenceDistance(caseSensitive: Boolean) extends DistanceCalculator with InverseConfidenceCalculator {
  def distance(label: String, thesaurusLabel: String): Option[Int] = {
    Some(applyDistanceFunction(label, thesaurusLabel, LongestCommonSeq.distance))
  }

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Int): Double = {
    doCalculateConfidence(distance(label, thesaurusLabel).getOrElse(0), maxThreshold)
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Int): Boolean = {
    doToBeFiltered(distance(label, thesaurusLabel).getOrElse(0), maxThreshold)
  }
}

object DistanceCalculatorFactory {
  def apply(distanceCalculatorName: Option[String], caseSensitive: Boolean): DistanceCalculator = distanceCalculatorName match {
    case Some(name) =>
      if(name == "Levenshtein") LevenshteinDistance(caseSensitive)
      else if(name == "Damerau-Levenshtein") DamerauLevenshteinDistance(caseSensitive)
      else if(name == "Hamming") HammingDistance(caseSensitive)
      else if(name == "LongestCommonSubsequence") LongestCommonSubsequenceDistance(caseSensitive)
      else throw new Exception(name + "is not a valid algorithm for distance calculation")
    case None => LevenshteinDistance(caseSensitive)
  }
}


