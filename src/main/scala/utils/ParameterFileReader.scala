package utils

import org.json4s.JsonAST.JArray
import org.json4s.{DefaultFormats, JField, JInt, JObject, JString}
import org.json4s.native.JsonMethods.parse

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ParameterFileReader {

  def readForOtomoto(name: String, os: String): List[(String, String, BigInt, BigInt)] = {
    val os: String = System.getProperty("os.name")


    implicit val formats: DefaultFormats.type = DefaultFormats

    val parametersFile = os match {
      case x if x.startsWith("Windows") => scala.io.Source.fromFile(System.getProperty("user.dir") + s"\\data\\$name")
      case x if x.startsWith("Linux") => scala.io.Source.fromFile(System.getProperty("user.dir") + s"/data/$name")
      case _ => scala.io.Source.fromFile(System.getProperty("user.dir") + s"/data/$name")
    }

    val parameters = parametersFile.getLines().mkString
    parametersFile.close()
    val json = parse(parameters)
    val otomotoJson = (json \ "otomoto").asInstanceOf[JArray]

    for {
      JObject(child) <- otomotoJson
      JField("manufacturer", JString(manufacturer)) <- child
      JField("model", JString(model)) <- child
      JField("startYear", JInt(startYear)) <- child
      JField("endYear", JInt(endYear)) <- child
    } yield (manufacturer, model, startYear, endYear)

  }

  def readForMobileDe(name: String): List[(String, String, BigInt, BigInt, BigInt, BigInt)] = {
    val parametersFile = scala.io.Source.fromFile(System.getProperty("user.dir") + s"\\$name")
    val parameters = parametersFile.getLines().mkString
    parametersFile.close()
    val json = parse(parameters)
    val mobiledeJson = (json \ "mobilede").asInstanceOf[JArray]

    for {
      JObject(child) <- mobiledeJson
      JField("manufacturer", JString(manufacturer)) <- child
      JField("manufacturerId", JInt(manufacturerId)) <- child
      JField("model", JString(model)) <- child
      JField("modelId", JInt(modelId)) <- child
      JField("startYear", JInt(startYear)) <- child
      JField("endYear", JInt(endYear)) <- child
    } yield (manufacturer, model, manufacturerId, modelId, startYear, endYear)
  }

}
