package engines

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import scala.collection.mutable

class MOBILEDEScrapingEngine {

  def initiateMOBILEDEScraping(searchParameters: (String, String, BigInt, BigInt),
                               filename: String,
                               withPhotos: Boolean): Unit = {

    var pageIteration: Int = 1
    var articlesAmount: Int = 0

    val browser = JsoupBrowser()

    val manufacturer = searchParameters._1
    val model = searchParameters._2
    val startYear = searchParameters._3
    val endYear = searchParameters._4

    val arguments = mutable.Map(
      "makeModelVariant1Make" -> manufacturer,
      "makeModelVariant1Model" -> model,
    )

    val manufacturerStartYearToYearLink: String = s"s"
    val formLink: String = "https://www.mobile.de/pl"


    val page = browser.get(formLink)
    val form = page


  }

}
