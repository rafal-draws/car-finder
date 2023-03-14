ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.5"


libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % "3.0.0",
  "org.json4s" %% "json4s-native" % "4.1.0-M2"
)

lazy val root = (project in file("."))
  .settings(
    name := "i_want_a_ls"
  )