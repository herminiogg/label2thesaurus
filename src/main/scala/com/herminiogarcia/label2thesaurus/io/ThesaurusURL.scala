package com.herminiogarcia.label2thesaurus.io

import java.net.URL

sealed trait ThesaurusURL {
  val url: URL
}

case class FileURL(url: URL) extends ThesaurusURL
case class SPARQLEndpoint(url: URL) extends ThesaurusURL
