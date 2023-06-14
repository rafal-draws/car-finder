package aggregators

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

class OTOMOTOArticle(link: String, browser: Browser, scrapDate: String) {

  private val articleLink: String = link
  private val articleBody: Element = browser.get(link) >> element("body")
  private val metadata: List[String] = articleBody >> elementList(".offer-meta__value") >> text("span")

  private val price: String = try articleBody >> text(".offer-price__number") catch {
    case e: NoSuchElementException =>
      println(s"unable to receive the price from $articleLink")
      "0"
  }

  private val id: String = metadata lift 1 getOrElse "null"
  private val date: String = metadata lift 2 getOrElse "null"

  private val title: String = link.substring(30, link.length - 14)

  private val yearKilometrageFueltypeBodytype: List[String] = articleBody >> elementList(".offer-main-params__item") >> allText take 4

  private val location: String = articleBody >> text(".seller-card__links__link__cta")

  private val detailKeys: List[String] = articleBody >> elementList(".offer-params__item") >> text("span")
  private val detailValues: List[String] = articleBody >> elementList(".offer-params__value") >> text

  private val details: Map[String, String] = (detailKeys zip detailValues) toMap

  private val equipment: List[String] = articleBody >> elementList(".parameter-feature-item") >> text

  private val description: String = articleBody >?> text(".offer-description__description") getOrElse "no desc available"

  private val photoDownloadLinks: List[String] = articleBody >?> elementList("img").map(_ >?> attr("data-lazy") flatten) getOrElse List.empty

  def toMap: Map[String, Any] = Map(
      "id" -> id,
      "date" -> date,
      "scrapDate" -> scrapDate,
      "title" -> title,
      "price" -> price,
      "url" -> articleLink,
      "photos" -> photoDownloadLinks,
      "location" -> location,
      "year" -> yearKilometrageFueltypeBodytype.head,
      "kilometers" -> yearKilometrageFueltypeBodytype(1),
      "fueltype" -> yearKilometrageFueltypeBodytype(2),
      "body" -> yearKilometrageFueltypeBodytype(3),
      "details" -> details,
      "equipment" -> equipment,
      "description" -> description)

  def toMapNoPhotos: Map[String, Any] = Map(
      "id" -> id,
      "date" -> date,
      "scrapDate" -> scrapDate,
      "title" -> title,
      "price" -> price,
      "url" -> articleLink,
      "location" -> location,
      "year" -> yearKilometrageFueltypeBodytype.head,
      "kilometers" -> yearKilometrageFueltypeBodytype(1),
      "fueltype" -> yearKilometrageFueltypeBodytype(2),
      "body" -> yearKilometrageFueltypeBodytype(3),
      "details" -> details,
      "equipment" -> equipment,
      "description" -> description)

  private def savePhotosToDrive(link: String): Unit = {
    println(s"photo saved! $link")
  }

}
