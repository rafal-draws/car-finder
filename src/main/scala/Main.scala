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
      println(data)

    }
  }


  def extractDataFromArticle(link: String, browser: Browser): Unit={

    val currentArticle = browser.get(link)

//    println(currentArticle)

    val photoDownloadLinks = currentArticle >?> elementList("img").map(_ >?> attr ("data-lazy") flatten)
    val photoDownloadLinksList: List[String] = photoDownloadLinks.getOrElse(List.empty).toList

    photoDownloadLinksList.foreach(saveThePhoto)



  }


  def extractArticleLink(article: Element): String = {

    val title = article >?> text("p")
    println(s"\n________________________\nTITLE: ${title.mkString}")

    val linkToAd = article >> element("h2 a") attr "href"

    linkToAd
  }

  def saveThePhoto(link: String): Unit ={
    //save the photo on location
  }
}
