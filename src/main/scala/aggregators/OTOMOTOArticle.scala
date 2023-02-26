package aggregators

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element


class OTOMOTOArticle (link: String, browser: Browser){

  private val articleLink: String = link
  private val articleBody: Element = browser.get(link) >> element("body")
  private val metadata: List[String] = articleBody >> elementList(".offer-meta__value") >> text("span")

  private val id: String = metadata lift 2 getOrElse "null"
  private val date: String = metadata lift 1 getOrElse "null"
  private val title: String = link.substring(30, link.length - 14)

  private val yearKilometrageFueltypeBodytype: List[String] = articleBody >> elementList(".offer-main-params__item") >> allText take 4

  private val location: String = articleBody >> text(".seller-card__links__link__cta")

  private val detailKeys: List[String] = articleBody >> elementList(".offer-params__item") >> text ("span")
  private val detailValues: List[String] = articleBody >> elementList(".offer-params__value") >> text

  private val details: Map[String, String] = (detailKeys zip detailValues) toMap

  private val equipment: List[String] = articleBody >> elementList(".parameter-feature-item") >> text

  private val description: String = articleBody >?> text(".offer-description__description") getOrElse "no desc available"

  private val photoDownloadLinks: List[String] = articleBody >?> elementList("img").map(_ >?> attr("data-lazy") flatten) getOrElse List.empty



  def toSeq: Seq[Any] = Seq(id, date, title, articleLink, photoDownloadLinks, location, yearKilometrageFueltypeBodytype, details, equipment, description)
  private def savePhotosToDrive(link: String): Unit = {
    println(s"photo saved! $link")
  }

}
