
name := "label2thesaurus"

organization := "com.herminiogarcia"

version := "0.1.0"

scalaVersion := "2.13.8"

libraryDependencies += "org.apache.jena" % "apache-jena-libs" % "3.8.0" pomOnly()

libraryDependencies += "org.apache.jena" % "jena-arq" % "3.8.0"

libraryDependencies += "com.github.vickumar1981" %% "stringdistance" % "1.2.6"

libraryDependencies += "info.picocli" % "picocli" % "4.0.4"

libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.2.11" % "test"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}