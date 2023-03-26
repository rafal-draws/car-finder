package utils

import java.io.{File, FileWriter, RandomAccessFile}

class OutputGenerator {

  def createOutputFile(filename: String): Unit ={
    val fw = new FileWriter(filename, true)
    try fw.write("[")
    finally fw.close()
  }

  def endOutputFile(filename: String): Unit = {
    val fw = new FileWriter(filename, true)
    try fw.write("]")
    finally fw.close()
  }

  def appendOutput(article: String, filename: String): Unit = {
    val fw = new FileWriter(filename, true)
    try fw.write(article + ",")
    finally fw.close()
  }

  def removeComma(filename: String): Unit = {
    val file = new RandomAccessFile(new File(filename), "rw")
    file.setLength(file.length() - 1)
    file.close()

  }

}
