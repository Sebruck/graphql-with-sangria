import Dependencies._

name := "hc-graph"

organization in ThisBuild := "com.holidaycheck.graph"
version in ThisBuild := "0.1-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.8"
scalafmtVersion in ThisBuild := "1.2.0"
scalafmtOnCompile in ThisBuild := true

lazy val api = project
  .in(file("api"))
  .settings(
    libraryDependencies ++= apiDependencies
  )
