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

import java.io.{FileWriter, IOException}
import java.net.SocketTimeoutException

class OTOMOTOScrapingEngine {

  /*
  * Assuming that every link provides at least one page of articles, it must collect all visible articles.
  * First page does not contain page list buttons, therefore it wont find pagination-step-forwards element
  * Although, the nextPageButtonClass becomes "Last Page". Therefore, it wont be "continuing" the scraping,
  * But will end on the first, initial iteration.
   */


  def initiateOTOMOTOScraping(searchParameters: (String, String, BigInt, BigInt),
                              filename: String,
                              withPhotos: Boolean): Unit = {

    var pageIteration: Int = 1
    var articlesAmount: Int = 0

    val browser = JsoupBrowser()

    val manufacturer = searchParameters._1
    val model = searchParameters._2
    val startYear = searchParameters._3
    val endYear = searchParameters._4

    val manufacturerStartYearToYearLink: String = s"https://www.otomoto.pl/osobowe/$manufacturer/$model/od-$startYear?search%5Bfilter_float_year%3Ato%5D=$endYear"

    val initPage = browser.get(manufacturerStartYearToYearLink + "&page=1")
    var nextPageButtonClass = initPage >?> element("li[data-testid='pagination-step-forwards']") >> attr("class") getOrElse "Last Page"

    println(s"Scraping initiated: $manufacturer, $model, years: $startYear - $endYear\n link: $manufacturerStartYearToYearLink")


    do {
      try {
        val page = browser.get(manufacturerStartYearToYearLink + s"&page=$pageIteration")
        nextPageButtonClass = page >?> element("li[data-testid='pagination-step-forwards']") >> attr("class") getOrElse "Last Page"

        val articles: List[Element] = page >> elementList("main article")
        for (article <- articles) {

          val articleLink: String = article >> element("h2 a") attr "href"

          //TODO CLEANUP JSON OUTPUT
          val currentArticleSeq = {
            val currentArticle = new OTOMOTOArticle(articleLink, browser)
            if (withPhotos) currentArticle.toMap else currentArticle.toMapNoPhotos
          }

          implicit val formats: AnyRef with Formats = Serialization.formats(NoTypeHints)

          val articleJson = write(currentArticleSeq)

          val fw = new FileWriter(filename, true)
          try fw.write(articleJson + ",")
          finally fw.close()

          articlesAmount += 1
        }

      } catch {

          case e: HttpStatusException => println(s"Unfortunately, article couldn't be fetched due to article expiration")
          case e: StringIndexOutOfBoundsException => println(s"Unfortunately, article couldn't be fetched due to link expiration")
          case e: IOException => println(s"Too many redirects occured trying to load URL")
          case e: NoSuchElementException => "http://otomoto.pl"
          case e: SocketTimeoutException => println("Socket Timed out!")
      }


      pageIteration += 1

    } while (!(nextPageButtonClass contains "pagination-item__disabled") && !(nextPageButtonClass eq "Last Page"))

    println(s"Articles fetched: $articlesAmount")
    println(s"Scraping finished.\n")
  }

}
