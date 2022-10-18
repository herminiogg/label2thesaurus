ThisBuild / organization := "com.herminiogarcia"
ThisBuild / organizationName := "herminiogarcia"
ThisBuild / organizationHomepage := Some(url("https://herminiogarcia.com"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/herminiogg/label2thesaurus"),
    "scm:git@github.com:herminiogg/label2thesaurus.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "herminiogg",
    name  = "Herminio Garcia Gonzalez",
    email = "herminio@herminiogarcia.com",
    url   = url("https://herminiogarcia.com")
  )
)

ThisBuild / description := "label2thesaurus: identification and reconciliation of controlled vocabulary terms from free text labels based on string similarity algorithms"
ThisBuild / licenses := List("MIT License" -> new URL("https://opensource.org/licenses/MIT"))
ThisBuild / homepage := Some(url("https://github.com/herminiogg/label2thesaurus"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

ThisBuild / publishMavenStyle := true