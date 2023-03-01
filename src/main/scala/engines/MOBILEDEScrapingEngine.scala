package engines

import aggregators.MOBILEDEArticle
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.element

import scala.collection.mutable

class MOBILEDEScrapingEngine {

  def initiateMOBILEDEScraping(searchParameters: (String, String, BigInt, BigInt, BigInt, BigInt),
                               filename: String,
                               withPhotos: Boolean): Unit = {

    var pageIteration: Int = 1
    var articlesAmount: Int = 0

    val browser = JsoupBrowser()

    val manufacturer = searchParameters._1
    val model = searchParameters._2
    val manufacturerId = searchParameters._3
    val modelId = searchParameters._4
    val startYear = searchParameters._5
    val endYear = searchParameters._6

    val manufacturerStartYearToYearLink: String = s"https://www.mobile.de/pl/samochod/$manufacturer-$model/vhc:car,pgn:$pageIteration,pgs:50,ms1:${manufacturerId}_${modelId}_,frn:$startYear,frx:$endYear"

    val initPage = browser.get(manufacturerStartYearToYearLink)

    var nextPageButtonClass = initPage >?> element(".pagination-nav.pagination-nav-right.btn.btn--muted.btn--s") match{
      case Some(x) => true
      case None => false
    }

    println(s"Scraping initiated: $manufacturer, $model, years: $startYear - $endYear")



    do {
      val link = s"https://www.mobile.de/pl/samochod/$manufacturer-$model/vhc:car,pgn:$pageIteration,pgs:50,ms1:${manufacturerId}_${modelId}_,frn:$startYear,frx:$endYear"
      println(s"iteration : $pageIteration\nlink: $link")

      val page = browser.get(link)

      val articles = page >> elementList("a[data-track-segment='car']")


      for (article <- articles) {

        val articleLink = article >> attr("href")
        val titleElement: Option[Element] = article >> elementList("h3") find {
          e => e.attr("class").contains("vehicle-title")
        }
        val title = try {
          titleElement >> text("h3") getOrElse "no title available"
        }

        val currentArticle = new MOBILEDEArticle("https://mobile.de" + articleLink, browser)
        println(currentArticle.toMap)

      }



      nextPageButtonClass = page >?> element(".pagination-nav.pagination-nav-right.btn.btn--muted.btn--s") match {
        case Some(x) => true
        case None => false
      }
      pageIteration += 1
    } while (nextPageButtonClass)



    println(s"Articles fetched: $articlesAmount")
    println("Scraping finished")
  }


}
