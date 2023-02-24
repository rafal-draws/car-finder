import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser.{JsoupDocument, JsoupElement}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.text
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


  }

  def initateScraping(link: String): Unit = {
    println(s"______________________\n\nscraping started.\n$link\npage: 1")

    val searchBrowser = JsoupBrowser()
    val page = searchBrowser.get(link)
    var nextPageButtonClass = page >?> element("li[data-testid='pagination-step-forwards']") >> attr("class") getOrElse "error"

    println(s"nextPageButtonclass = $nextPageButtonClass")


    // first page iteration
    val articles: List[Element] = page >> elementList("main article")

    for (article <- articles) {

      val articleLink: String = article >> element("h2 a") attr "href"
      val currentArticle = new OTOMOTOArticle(articleLink, searchBrowser)
      val currentArticleSeq = currentArticle.toSeq

      //append avro
      println(currentArticleSeq)

    }


    var pageIteration: Int = 2
    while (!(nextPageButtonClass contains "pagination-item__disabled")) {


      val page = searchBrowser.get(link + s"&page=$pageIteration")

      println(s"______________________\n\nscraping continues\n$link&page=$pageIteration\npage: $pageIteration")
      nextPageButtonClass = page >?> element("li[data-testid='pagination-step-forwards']") >> attr("class") getOrElse "Error"
      println(s"nextPageButtonclass = $nextPageButtonClass")

      val articles: List[Element] = page >> elementList("main article")
      for (article <- articles) {

        val articleLink: String = article >> element("h2 a") attr "href"

        val currentArticle = new OTOMOTOArticle(articleLink, searchBrowser)
        val currentArticleSeq = currentArticle.toSeq

        //append avro
        println(currentArticleSeq)

      }
      pageIteration += 1



    }
      println("______________________\n\nscraping ended successfully")
  }


}


