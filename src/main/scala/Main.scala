import org.json4s._
import org.json4s.native.JsonMethods._
import engines.OTOMOTOScrapingEngine

object Main {

  def main(args: Array[String]) : Unit = {
    val parametersFile = scala.io.Source.fromFile(System.getProperty("user.dir") + "\\parameters.json")
    val parameters = parametersFile.getLines().mkString
    parametersFile.close()

    val json = parse(parameters)

    val searchParameters: List[(String, String, BigInt)] = for {
      JObject(child) <- json
      JField("manufacturer", JString(manufacturer)) <- child
      JField("model", JString(model)) <- child
      JField("to", JInt(to)) <- child
    } yield (manufacturer, model, to)



    for (searchParameter <- searchParameters){
      val manufacturer = searchParameter._1
      val model = searchParameter._2
      val to = searchParameter._3

      val otomotoScrapingEngine: OTOMOTOScrapingEngine = new OTOMOTOScrapingEngine()

      val link: String = s"https://www.otomoto.pl/osobowe/$manufacturer/$model?search%5Bfilter_float_year%3Ato%5D=$to"

      otomotoScrapingEngine.initiateOTOMOTOScraping(link)


    }
  }

}


