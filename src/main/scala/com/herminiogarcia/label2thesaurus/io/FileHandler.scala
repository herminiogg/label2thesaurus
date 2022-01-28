package com.herminiogarcia.label2thesaurus.io

import java.io.{BufferedWriter, File, FileWriter}

class FileHandler(path: String) {

  def getContent(): String = {
    val fileBuffer = scala.io.Source.fromFile(path)
    val content = fileBuffer.mkString
    fileBuffer.close()
    content
  }

  def write(content: String) {
    val file = new File(path)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(content)
    bw.close()
  }

  def splitByLine(): List[String] = {
    getContent().replace("\r\n", "\n").replace("\r", "\n").split("\n").toList
  }

}
