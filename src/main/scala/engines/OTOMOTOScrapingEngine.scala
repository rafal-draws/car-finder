package engines

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element
import aggregators.OTOMOTOArticle
import org.jsoup.HttpStatusException
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import utils.OutputGenerator

import java.io.IOException
import java.net.SocketTimeoutException

class OTOMOTOScrapingEngine {

  def initiateOTOMOTOScraping(searchParameters: (String, String, BigInt, BigInt),
                              filename: String,
                              withPhotos: Boolean): Unit = {
    var pageIteration: Int = 1
    var articlesCount: Int = 0
    var nextPageButtonClass: String = "empty"

    val outputGenerator: OutputGenerator = new OutputGenerator()
    val browser = JsoupBrowser()

    val manufacturer = searchParameters._1
    val model = searchParameters._2
    val startYear = searchParameters._3
    val endYear = searchParameters._4

    val manufacturerStartYearToYearLink: String = s"https://www.otomoto.pl/osobowe/$manufacturer/$model/od-$startYear?search%5Bfilter_float_year%3Ato%5D=$endYear"



    println(s"Scraping initiated: $manufacturer, $model, years: $startYear - $endYear\nlink: $manufacturerStartYearToYearLink")
    do {
      try {
        val page = browser.get(manufacturerStartYearToYearLink + s"&page=$pageIteration")
        nextPageButtonClass = page >?> element("li[data-testid='pagination-step-forwards']") >> attr("class") getOrElse "Last Page"

        val articles: List[Element] = page >> elementList("main article")
        for (article <- articles) {

          val articleLink: String = article >> element("h2 a") attr "href"

          val currentArticleSeq = {
            val currentArticle = new OTOMOTOArticle(articleLink, browser)
            if (withPhotos) currentArticle.toMap else currentArticle.toMapNoPhotos
          }

          implicit val formats: AnyRef with Formats = Serialization.formats(NoTypeHints)

          val articleJson = write(currentArticleSeq)
          outputGenerator.appendOutput(articleJson, filename)


          articlesCount += 1
        }

      } catch {
          case e: HttpStatusException => println(s"Unfortunately, article couldn't be fetched due to article expiration")
          case e: StringIndexOutOfBoundsException => println(s"Unfortunately, article couldn't be fetched due to link expiration")
          case e: IOException => println(s"Too many redirects occured trying to load URL")
          case e: NoSuchElementException => println("Element couldn't be found, skipping")
          case e: SocketTimeoutException => println("Socket Timed out!")
      }


      pageIteration += 1

    } while (!(nextPageButtonClass contains "pagination-item__disabled") && !(nextPageButtonClass eq "Last Page"))

    println(s"Articles fetched: $articlesCount")
    println(s"Scraping finished.\n")
  }

}
