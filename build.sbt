name := """rest-demo"""
organization := "com.arpit"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

libraryDependencies += jdbc

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.arpit.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.arpit.binders._"
// https://mvnrepository.com/artifact/org.mockito/mockito-core
libraryDependencies += "org.mockito" % "mockito-core" % "2.8.47" % "test"
