ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.4"

lazy val root = (project in file("."))
  .settings(
    name := "i_want_a_ls",
    libraryDependencies ++= Seq(
      "net.ruippeixotog" %% "scala-scraper" % "3.0.0"
    )
  )
