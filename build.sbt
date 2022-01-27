name := "label2thesaurus"

version := "0.1"

scalaVersion := "2.13.8"

idePackagePrefix := Some("com.herminiogarcia.label2thesaurus")

libraryDependencies += "org.apache.jena" % "apache-jena-libs" % "3.8.0" pomOnly()

libraryDependencies += "org.apache.jena" % "jena-arq" % "3.8.0"

libraryDependencies += "com.github.vickumar1981" %% "stringdistance" % "1.2.6"
