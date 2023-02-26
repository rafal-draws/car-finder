import org.json4s._
import org.json4s.native.JsonMethods._
import engines.OTOMOTOScrapingEngine
import utils.ParameterReader

object Main {

  def main(args: Array[String]) : Unit = {
    val otomotoScrapingEngine: OTOMOTOScrapingEngine = new OTOMOTOScrapingEngine()
    val parameterReader: ParameterReader = new ParameterReader()

    val parameters: List[(String, String, BigInt)] = parameterReader.readParams("parameters.json")

    for (searchParameterList <- parameters){

      val manufacturer = searchParameterList._1
      val model = searchParameterList._2
      val to = searchParameterList._3

      val link: String = s"https://www.otomoto.pl/osobowe/$manufacturer/$model?search%5Bfilter_float_year%3Ato%5D=$to"

      otomotoScrapingEngine.initiateOTOMOTOScraping(link, "out.json")

    }
  }

}


