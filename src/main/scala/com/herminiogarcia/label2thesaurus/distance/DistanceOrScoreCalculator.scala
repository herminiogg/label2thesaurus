package com.herminiogarcia.label2thesaurus.distance

import com.github.vickumar1981.stringdistance.StringDistance.{Cosine, Damerau, DiceCoefficient, Hamming, Jaro, Levenshtein, LongestCommonSeq}
import com.github.vickumar1981.stringdistance.StringSound.{Metaphone, Soundex}

sealed trait DistanceOrScoreCalculator {
  val caseSensitive: Boolean

  def distance(label: String, thesaurusLabel: String): Option[Int]
  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Double): Double
  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Double): Boolean
  protected def caseSensitive(label: String): String = {
    if(!caseSensitive) label.toLowerCase else label
  }
  protected def applyDistanceFunction(label: String, thesaurusLabel: String, distanceFunction: (String, String) => Int): Int = {
    distanceFunction(caseSensitive(label), caseSensitive(thesaurusLabel))
  }
}

trait NormalConfidenceCalculator {
  protected def doCalculateConfidence(distance: Int, maxThreshold: Double): Double = {
    (maxThreshold - distance) / maxThreshold.toDouble * 100.0
  }

  protected def doToBeFiltered(distance: Int,  maxThreshold: Double): Boolean = distance < maxThreshold
}

trait InverseConfidenceCalculator {
  protected def doCalculateConfidence(distance: Int, minThreshold: Double): Double = {
    val confidence = (distance - minThreshold) / minThreshold * 100.0
    if(distance < minThreshold) 0 else confidence
  }

  protected def doToBeFiltered(distance: Int,  maxThreshold: Double): Boolean = distance > maxThreshold
}

trait ScoreCalculator extends DistanceOrScoreCalculator {

  def distance(label: String, thesaurusLabel: String): Option[Int] = None

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Double): Double = {
    calculateScore(caseSensitive(label), caseSensitive(thesaurusLabel))
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Double): Boolean = {
    calculateConfidence(label, thesaurusLabel, maxThreshold) > maxThreshold
  }

  def calculateScore(label: String, thesaurusLabel: String): Double

}

trait BooleanScoreCalculator {
  def boolean2Double(result: Boolean): Double = if(result) 1 else 0
}

case class LevenshteinDistance(caseSensitive: Boolean) extends DistanceOrScoreCalculator with NormalConfidenceCalculator {

  def distance(label: String, thesaurusLabel: String): Option[Int] = {
    Some(applyDistanceFunction(label, thesaurusLabel, Levenshtein.distance))
  }

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Double): Double = {
    doCalculateConfidence(distance(label, thesaurusLabel).getOrElse(maxThreshold.toInt), maxThreshold)
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Double): Boolean = {
    doToBeFiltered(distance(label, thesaurusLabel).getOrElse(maxThreshold.toInt), maxThreshold)
  }
}

case class DamerauLevenshteinDistance(caseSensitive: Boolean) extends DistanceOrScoreCalculator with NormalConfidenceCalculator {
  def distance(label: String, thesaurusLabel: String): Option[Int] = {
    Some(applyDistanceFunction(label, thesaurusLabel, Damerau.distance))
  }

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Double): Double = {
    doCalculateConfidence(distance(label, thesaurusLabel).getOrElse(maxThreshold.toInt), maxThreshold)
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Double): Boolean = {
    doToBeFiltered(distance(label, thesaurusLabel).getOrElse(maxThreshold.toInt), maxThreshold)
  }
}

case class HammingDistance(caseSensitive: Boolean) extends DistanceOrScoreCalculator with NormalConfidenceCalculator {
  def distance(label: String, thesaurusLabel: String): Option[Int] = {
    val distance = applyDistanceFunction(label, thesaurusLabel, Hamming.distance)
    if(distance < 0) None else Some(distance)
  }

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Double): Double = {
    doCalculateConfidence(distance(label, thesaurusLabel).getOrElse(maxThreshold.toInt), maxThreshold)
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Double): Boolean = {
    doToBeFiltered(distance(label, thesaurusLabel).getOrElse(maxThreshold.toInt), maxThreshold)
  }
}

case class LongestCommonSubsequenceDistance(caseSensitive: Boolean) extends DistanceOrScoreCalculator with InverseConfidenceCalculator {
  def distance(label: String, thesaurusLabel: String): Option[Int] = {
    Some(applyDistanceFunction(label, thesaurusLabel, LongestCommonSeq.distance))
  }

  def calculateConfidence(label: String, thesaurusLabel: String, maxThreshold: Double): Double = {
    doCalculateConfidence(distance(label, thesaurusLabel).getOrElse(0), maxThreshold)
  }

  def toBeFiltered(label: String, thesaurusLabel: String, maxThreshold: Double): Boolean = {
    doToBeFiltered(distance(label, thesaurusLabel).getOrElse(0), maxThreshold)
  }
}

case class CosineScore(caseSensitive: Boolean) extends ScoreCalculator {
  override def calculateScore(label: String, thesaurusLabel: String): Double = Cosine.score(label, thesaurusLabel)
}

case class DamerauLevenshteinScore(caseSensitive: Boolean) extends ScoreCalculator {
  override def calculateScore(label: String, thesaurusLabel: String): Double = Damerau.score(label, thesaurusLabel)
}

case class DiceScore(caseSensitive: Boolean) extends ScoreCalculator {
  override def calculateScore(label: String, thesaurusLabel: String): Double = DiceCoefficient.score(label, thesaurusLabel)
}

case class HamminScore(caseSensitive: Boolean) extends ScoreCalculator {
  override def calculateScore(label: String, thesaurusLabel: String): Double = Hamming.score(label, thesaurusLabel)
}

case class JaroScore(caseSensitive: Boolean) extends ScoreCalculator {
  override def calculateScore(label: String, thesaurusLabel: String): Double = Jaro.score(label, thesaurusLabel)
}

case class LevenshteinScore(caseSensitive: Boolean) extends ScoreCalculator {
  override def calculateScore(label: String, thesaurusLabel: String): Double = Levenshtein.score(label, thesaurusLabel)
}

case class MetaphoneScore(caseSensitive: Boolean) extends ScoreCalculator with BooleanScoreCalculator {
  override def calculateScore(label: String, thesaurusLabel: String): Double = boolean2Double(Metaphone.score(label, thesaurusLabel))
}

case class SoundexScore(caseSensitive: Boolean) extends ScoreCalculator with BooleanScoreCalculator {
  override def calculateScore(label: String, thesaurusLabel: String): Double = boolean2Double(Soundex.score(label, thesaurusLabel))
}

object DistanceCalculatorFactory {
  def apply(distanceCalculatorName: Option[String], caseSensitive: Boolean): DistanceOrScoreCalculator = distanceCalculatorName match {
    case Some(name) =>
      if(name == "Levenshtein") LevenshteinDistance(caseSensitive)
      else if(name == "Damerau-Levenshtein") DamerauLevenshteinDistance(caseSensitive)
      else if(name == "Hamming") HammingDistance(caseSensitive)
      else if(name == "LongestCommonSubsequence") LongestCommonSubsequenceDistance(caseSensitive)
      else throw new Exception(name + "is not a valid algorithm for distance calculation")
    case None => LevenshteinDistance(caseSensitive)
  }
}

object ScoreCalculatorFactory {
  def apply(scoreCalculatorName: Option[String], caseSensitive: Boolean): DistanceOrScoreCalculator = scoreCalculatorName match {
    case Some(name) =>
      if(name == "Cosine") CosineScore(caseSensitive)
      else if(name == "Damerau-Levenshtein") DamerauLevenshteinScore(caseSensitive)
      else if(name == "Dice") DiceScore(caseSensitive)
      else if(name == "Hamming") HamminScore(caseSensitive)
      else if(name == "Jaro") JaroScore(caseSensitive)
      else if(name == "Levenshtein") LevenshteinScore(caseSensitive)
      else if(name == "Metaphone") MetaphoneScore(caseSensitive)
      else if(name == "Soundex") SoundexScore(caseSensitive)
      else throw new Exception(name + "is not a valid algorithm for score calculation")
    case None => CosineScore(caseSensitive)
  }
}


