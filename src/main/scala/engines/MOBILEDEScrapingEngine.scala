package engines

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

class MOBILEDEScrapingEngine {

  def initiateMOBILEDEScraping(searchParameters: (String, String, BigInt, BigInt)): Unit = {

    var pageIteration: Int = 1
    var articlesAmount: Int = 0


    val manufacturer = searchParameters._1
    val model = searchParameters._2
    val startYear = searchParameters._3
    val endYear = searchParameters._4

    val manufacturerStartYearToYearLink: String = s"https://www.mobile.de/pl/samochod/$manufacturer-$model/vhc:car,pgn:1,pgs:50,ms1:17200_-11_,frn:$startYear,frx:$endYear"

    val searchBrowser = JsoupBrowser()
    val page = searchBrowser.get(manufacturerStartYearToYearLink)

  }

}
