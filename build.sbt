ThisBuild / organization := "com.herminiogarcia"

lazy val label2thesaurus = project
  .in(file("."))
  .settings(
    name := "label2thesaurus",
    version := "0.1.0",
    scalaVersion := "3.2.0",
    crossScalaVersions := Seq("2.12.17", "2.13.9", "3.2.0"),
    libraryDependencies ++= Seq(
      "org.apache.jena" % "apache-jena-libs" % "3.8.0" pomOnly(),
      "org.apache.jena" % "jena-arq" % "3.8.0",
      ("com.github.vickumar1981" %% "stringdistance" % "1.2.6").cross(CrossVersion.for3Use2_13),
      "info.picocli" % "picocli" % "4.0.4",
      "org.scalatest" %% "scalatest" % "3.2.11" % "test"
    ),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "services", xs @ _*) => MergeStrategy.concat
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    }
  )