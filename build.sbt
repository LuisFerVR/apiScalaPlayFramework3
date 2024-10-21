name := """apiScalaPlayFramework"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.15"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test

libraryDependencies ++= Seq(
  // Librerías de slick, para permitir las conexiones y las evoluciones
  "org.playframework" %% "play-slick" % "6.1.1",
  "org.playframework" %% "play-slick-evolutions" % "6.1.0",
  // Librería de la base de datos a utilizar, en este caso, la conexión se busca realizar con Postgres
  "org.postgresql" % "postgresql" % "42.7.3",

  // Dependencias de Play y Play JSON
  "com.typesafe.play" %% "play" % "3.0.5",
  "com.typesafe.play" %% "play-json" % "3.0.5",
)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
