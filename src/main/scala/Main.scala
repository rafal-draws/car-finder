import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser.{JsoupDocument, JsoupElement}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element
import org.jsoup.nodes.DocumentType

object Main {

  def main(args: Array[String]) : Unit = {

    val searchBrowser = JsoupBrowser()
    val otomotoLSSearch = searchBrowser.get("https://www.otomoto.pl/osobowe/lexus/ls?search%5Bfilter_float_year%3Ato%5D=2000")
    val articles = otomotoLSSearch >> elementList("main article")


    for (article <- articles){
      val link: String = extractArticleLink(article)
      val data = extractDataFromArticle(link, searchBrowser)
    }
  }



  def extractDataFromArticle(link: String, browser: Browser): Unit={

    println("\n______________________\n")

    val articleLink = link

    val articleBody = browser.get(link) >> element("body")

    val title = link.substring(30, link.length - 14)
    //    println(s"Title: $title")

    val photoDownloadLinks = articleBody >?> elementList("img").map(_ >?> attr("data-lazy") flatten)
    val photoDownloadLinksList: List[String] = photoDownloadLinks.getOrElse(List.empty)
    //    LINKI ze zdjeciami
    //    photoDownloadLinksList.foreach(saveThePhoto)

    val yearKilometrageFueltypeBodytype = articleBody >> elementList(".offer-main-params__item") >> allText take 4
    //    println(s"params: $yearKilometrageFueltypeBodytype")

    val location = articleBody >> text(".seller-card__links__link__cta")
    //    println(s"location: $location")

    val detailKeys = articleBody  >> elementList(".offer-params__item") >> text ("span")
    val detailValues = articleBody  >> elementList(".offer-params__value") >> text
    val details = (detailKeys zip detailValues) toMap

    println(details)

  }
  def extractArticleLink(article: Element): String = {
    val linkToAd = article >> element("h2 a") attr "href"
    linkToAd
  }

  def saveThePhoto(link: String): Unit ={
    println(link)
    //TODO save the photo
  }
}
