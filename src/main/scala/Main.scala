import engines.OTOMOTOScrapingEngine
import utils.ParameterFileReader

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main {

  def main(args: Array[String]) : Unit = {

    val filename = if (args.length >= 1) args(0) else
      "C:\\Users\\rafal\\Desktop\\data\\otomotoScrapedData"+ DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm").format(LocalDateTime.now) + ".json"
    val withPhotos: Boolean = if (args.length >= 2) try args(1).toBoolean else false

    val otomotoScrapingEngine: OTOMOTOScrapingEngine = new OTOMOTOScrapingEngine()
    val parameterFileReader: ParameterFileReader = new ParameterFileReader()

    val OTOMOTOSearchParametersList: List[(String, String, BigInt, BigInt)] = parameterFileReader.readForOtomoto("parameters.json")
    val MOBILEDESearchParametersList: List[(String, String, BigInt, BigInt)] = parameterFileReader.readForMobileDe("parameters.json")


    val startTime = System.nanoTime()


    for (searchParameters <- OTOMOTOSearchParametersList) {
      otomotoScrapingEngine.initiateOTOMOTOScraping(searchParameters, filename, withPhotos)
    }

    for (searchParameters <- MOBILEDESearchParametersList){
      println(searchParameters)
    }


    val endTime = System.nanoTime()
    val timeSpent = (endTime - startTime).toDouble / 1000000000.0
    println(s"Time taken: $timeSpent")
  }

}


