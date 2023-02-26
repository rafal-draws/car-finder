package engines

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element
import aggregators.OTOMOTOArticle
import org.jsoup.HttpStatusException


class OTOMOTOScrapingEngine {

  /*
  * Assuming that every link provides at least one page of articles, it must collect all visible articles.
  * First page does not contain page list buttons, therefore it wont find pagination-step-forwards element
  * Although, the nextPageButtonClass becomes "Last Page". Therefore, it wont be "continuing" the scraping,
  * But will end on the first, initial iteration.
   */
  def initiateOTOMOTOScraping(link: String): Unit = {

    val searchBrowser = JsoupBrowser()
    val page = searchBrowser.get(link + "&page=1")
    var nextPageButtonClass = page >?> element("li[data-testid='pagination-step-forwards']") >> attr("class") getOrElse "Last Page"

    var pageIteration: Int = 1


    do {
      val page = searchBrowser.get(link + s"&page=$pageIteration")

      println(s"______________________\nscraping:\n$link&page=$pageIteration\npage: $pageIteration")

      nextPageButtonClass = page >?> element("li[data-testid='pagination-step-forwards']") >> attr("class") getOrElse "Last Page"
      println(s"nextPageButtonclass = $nextPageButtonClass")

      val articles: List[Element] = page >> elementList("main article")
      for (article <- articles) {

        val articleLink: String = try {
          article >> element("h2 a") attr "href"
        } catch {
          case e: NoSuchElementException => "http://otomoto.pl"
        }

        val currentArticleSeq = try {
          val currentArticle = new OTOMOTOArticle(articleLink, searchBrowser)
          currentArticle.toSeq
        } catch {
          case e: HttpStatusException => println(s"Unfortunately, article couldn't be fetched due to article expiration -> $articleLink")
        }

        println(currentArticleSeq)
      }

      pageIteration += 1


    } while (!(nextPageButtonClass contains "pagination-item__disabled") && !(nextPageButtonClass eq "Last Page"))


    println("______________________\n\nscraping ended successfully")
  }

}
