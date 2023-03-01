package aggregators

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.{Element, ElementQuery}
import org.joda.time.DateTime

class MOBILEDEArticle(link: String, browser: Browser) {

  println(link)

  private val articleBody: Element = browser.get(link) >> element("body")

  private val id: String = link.substring(link.length - 14, link.length - 5)
  private val date: String = DateTime.now().toString("dd-MM-yyyy'T'hh:mm:ss")
  private val title: String = articleBody >> text("h1")

  private val location: Any = articleBody >> elementList("div") find {
    e => e.attr("class").contains("seller-box")
  }

  println(location)
  private val price: String = articleBody >> text(".buyer-currency.u-text-bold")

  private val technicals = articleBody >> elementList(".g-col-6")

  println(technicals)
  println(toMap)

  def toMap: Map[String, Any] = Map(
    id -> Map(
      "date" -> date,
      "title" -> title,
      "price" -> price,
      "url" -> link,
      "location" -> location,

    )
  )
}
