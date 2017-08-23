name := """rest-demo"""
organization := "com.arpit"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test,
  "com.h2database" % "h2" % "1.4.196",
  "org.mockito" % "mockito-core" % "2.8.47" % "test",
  "org.specs2" % "specs2-core_2.12" % "3.9.4" % "test",
  "org.specs2" % "specs2-mock_2.12" % "3.9.4" % "test")
