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

    val searchParameters: List[(String, String, BigInt)] = parameterFileReader.read("parameters.json")
    val startTime = System.nanoTime()

    for (searchParameter <- searchParameters) {
      val manufacturer = searchParameter._1
      val model = searchParameter._2
      val startYear = 1900 //TODO CHECK SEARCH PARAMETERS
      val to = searchParameter._3


      val manufacturerModelToYearLink: String = s"https://www.otomoto.pl/osobowe/$manufacturer/$model?search%5Bfilter_float_year%3Ato%5D=$to"
      val manufacturerModelStartYearToYearLink: String = s"https://www.otomoto.pl/osobowe/$manufacturer/$model/od-$startYear?search%5Bfilter_float_year%3Ato%5D=$to"


      println(s"Scraping initiated: $manufacturer, $model, to year: $to")

      otomotoScrapingEngine.initiateOTOMOTOScraping(manufacturerModelToYearLink, filename, withPhotos)

      println(s"Scraping finished.\n")

    }

    val endTime = System.nanoTime()
    val timeSpent = (endTime - startTime).toDouble / 1000000000.0
    println(s"Time taken: $timeSpent")
  }

}


