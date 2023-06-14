package utils


class FileNameCreator(date: String) {
  def returnFilepath(): String = {
    val os: String = System.getProperty("os.name")
    os match {
      case x if x.startsWith("Windows") => System.getProperty("user.dir") + "\\data\\" + date + ".json"
      case x if x.startsWith("Linux") => System.getProperty("user.dir") + "/data/" + date + ".json"
      case _ => System.getProperty("user.dir") + date + ".json"
    }
  }

}
