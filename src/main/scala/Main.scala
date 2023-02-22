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
//      if (args.contains("toYear")){
//     POSSIBLE OPTIONS VARIANT: toYear, fromYearToYear,
//      }


//    val manufacturer = "lexus"
//    val model = "ls"
//    val endYear = 2000
//    val startYear = null


    val manufacturer = "bmw"
    val model = "seria-3"
    val endYear = 2000
//    val startYear = null



    val link: String = s"https://www.otomoto.pl/osobowe/$manufacturer/$model?search%5Bfilter_float_year%3Ato%5D=$endYear"
    initateScraping(link)


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

  }

  def initateScraping(link: String): Unit = {

    val searchBrowser = JsoupBrowser()
    val page: searchBrowser.DocumentType = searchBrowser.get(link)

    val nextPageButton = page >?> element("li[title='Next Page']")
    println(nextPageButton)
    //TODO IF ELEMENT EXISTS - LOOP

    val articles: List[Element] = page >> elementList("main article")


    for (article <- articles) {

      val articleLink: String = article >> element("h2 a") attr "href"

      val currentArticle = new OTOMOTOArticle(articleLink, searchBrowser)
      val currentArticleSeq = currentArticle.toSeq

      println(currentArticleSeq)

    }

  }


}


