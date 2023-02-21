import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser.{JsoupDocument, JsoupElement}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.text
import org.jsoup.nodes.DocumentType
import aggregators.OTOMOTOArticle

object Main {

  def main(args: Array[String]) : Unit = {
//    require(args.length >= 3, "please provide at least three arguments!")



    val manufacturer = "lexus"
    val model = "ls"
    val endYear = 2000
    val startYear = null

    val searchBrowser = JsoupBrowser()
    val otomotoLSSearch = searchBrowser.get("https://www.otomoto.pl/osobowe/" + "lexus/ls?search%5Bfilter_float_year%3Ato%5D=2000")
    val articles = otomotoLSSearch >> elementList("main article")

//
//    case class Article(id: String,
//                       date: String,
//                       title: String,
//                       yearKilometrageFuelTypeBodyType: List[String],
//                       location: String,
//                       details: Map[String, String],
//                       equipment: List[String],
//                       description: String)
//

    for (article <- articles){

      val link: String = article >> element("h2 a") attr "href"

      val currentArticle = new OTOMOTOArticle(link, searchBrowser)
      val currentArticleSeq = currentArticle.toSeq

      println(currentArticleSeq)

      }
  }

}
