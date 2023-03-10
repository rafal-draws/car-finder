import engines.{MOBILEDEScrapingEngine, OTOMOTOScrapingEngine}
import utils.{OutputGenerator, ParameterFileReader}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main {

  def main(args: Array[String]) : Unit = {

    val os: String = System.getProperty("os.name")
    val filename = if (args.length >= 1) args(0) else os match {
      case x if x.startsWith("Windows") => System.getProperty("user.dir") + "\\data\\" + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm").format(LocalDateTime.now) + ".json"
      case x if x.startsWith("Linux") => System.getProperty("user.dir") + "/data/" + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm").format(LocalDateTime.now) + ".json"
      case _ => System.getProperty("user.dir") + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm").format(LocalDateTime.now) + ".json"
    }

    val withPhotos: Boolean = if (args.length >= 2) args(1).toBoolean else false

    val otomotoScrapingEngine: OTOMOTOScrapingEngine = new OTOMOTOScrapingEngine()
    val mobiledeScrapingEngine: MOBILEDEScrapingEngine = new MOBILEDEScrapingEngine()
    val parameterFileReader: ParameterFileReader = new ParameterFileReader()
    val outputGenerator: OutputGenerator = new OutputGenerator()

    val OTOMOTOSearchParametersList: List[(String, String, BigInt, BigInt)] = parameterFileReader.readForOtomoto("parameters.json", os)
//    val MOBILEDESearchParametersList: List[(String, String, BigInt, BigInt, BigInt, BigInt)] = parameterFileReader.readForMobileDe("parameters.json")


    val startTime = System.nanoTime()
    outputGenerator.createOutputFile(filename)

//    for (searchParameters <- MOBILEDESearchParametersList){
//      mobiledeScrapingEngine.initiateMOBILEDEScraping(searchParameters, filename, withPhotos)
//    }

    for (searchParameters <- OTOMOTOSearchParametersList) {
      otomotoScrapingEngine.initiateOTOMOTOScraping(searchParameters, filename, withPhotos)
    }

    val endTime = System.nanoTime()
    val timeSpent = (endTime - startTime).toDouble / 1000000000.0
    println(s"Time taken: $timeSpent")
    outputGenerator.removeComma(filename)
    outputGenerator.endOutputFile(filename)
  }

}


