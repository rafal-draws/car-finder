package engines

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element
import aggregators.OTOMOTOArticle


class OTOMOTOScrapingEngine {

  def initiateOTOMOTOScraping(link: String): Unit = {
    println(s"______________________\n\nscraping started.\n$link\npage: 1")

    val searchBrowser = JsoupBrowser()
    val page = searchBrowser.get(link)
    var nextPageButtonClass = page >?> element("li[data-testid='pagination-step-forwards']") >> attr("class") getOrElse "error"

    println(s"nextPageButtonclass = $nextPageButtonClass")


    // first page iteration due confirmation of next page button
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

        println(currentArticleSeq)
      }
      pageIteration += 1


    }
    println("______________________\n\nscraping ended successfully")
  }

}
