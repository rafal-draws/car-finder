import engines.OTOMOTOScrapingEngine
import utils.{OperatingSystemCheck, OutputGenerator, ParameterFileReader}

object Main {

  def main(args: Array[String]) : Unit = {

    val filename = if (args.length >= 1) args(0) else OperatingSystemCheck.returnFilepath()

    val withPhotos: Boolean = if (args.length >= 2) args(1).toBoolean else false

    val otomotoScrapingEngine: OTOMOTOScrapingEngine = new OTOMOTOScrapingEngine()
    val parameterFileReader: ParameterFileReader = new ParameterFileReader()
    val outputGenerator: OutputGenerator = new OutputGenerator()

    val otomotoSearchParametersList: List[(String, String, BigInt, BigInt)] = parameterFileReader.readForOtomoto("parameters.json", System.getProperty("os.name"))

    val startTime = System.nanoTime()

    outputGenerator.createOutputFile(filename)
    otomotoSearchParametersList.foreach(x => otomotoScrapingEngine.initiateOTOMOTOScraping(x, filename, withPhotos))

    val endTime = System.nanoTime()
    val timeSpent = (endTime - startTime).toDouble / 1000000000.0
    println(s"Time taken: $timeSpent")

    outputGenerator.removeComma(filename)
    outputGenerator.endOutputFile(filename)
  }

}


