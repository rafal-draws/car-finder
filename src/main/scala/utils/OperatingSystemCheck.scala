package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object OperatingSystemCheck {
  def returnFilepath(): String = {
    val os: String = System.getProperty("os.name")
    os match {
      case x if x.startsWith("Windows") => System.getProperty("user.dir") + "\\data\\" + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm").format(LocalDateTime.now) + ".json"
      case x if x.startsWith("Linux") => System.getProperty("user.dir") + "/data/" + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm").format(LocalDateTime.now) + ".json"
      case _ => System.getProperty("user.dir") + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm").format(LocalDateTime.now) + ".json"
    }
  }

}
