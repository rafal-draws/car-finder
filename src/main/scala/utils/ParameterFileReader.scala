package utils

import org.json4s.{JField, JInt, JObject, JString}
import org.json4s.native.JsonMethods.parse

class ParameterFileReader {

  def read(name: String): List[(String, String, BigInt)] = {
    val parametersFile = scala.io.Source.fromFile(System.getProperty("user.dir") + s"\\$name")
    val parameters = parametersFile.getLines().mkString
    parametersFile.close()
    val json = parse(parameters)

    for {
      JObject(child) <- json
      JField("manufacturer", JString(manufacturer)) <- child
      JField("model", JString(model)) <- child
      JField("to", JInt(to)) <- child
    } yield (manufacturer, model, to)
  }

}
