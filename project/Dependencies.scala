import sbt._

object Dependencies {
  val akkaHttpVersion = "10.1.9"
  val akkaVersion     = "2.5.23"

  lazy val scalatest = Seq(
    "org.scalatest" %% "scalatest" % "3.0.8"
  )

  lazy val akka = Seq(
    "com.typesafe.akka" %% "akka-slf4j"       % akkaVersion,
    "com.typesafe.akka" %% "akka-stream"      % akkaVersion,
    "com.typesafe.akka" %% "akka-http"        % akkaHttpVersion,
    "com.holidaycheck"  %% "easy-akka-client" % "0.0.9"
  )

  lazy val akkaHttpCirce = Seq(
    "de.heikoseeberger" %% "akka-http-circe" % "1.27.0"
  )

  lazy val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % "0.11.1") ++ Seq("io.circe" %% "circe-optics" % "0.11.0")

  lazy val sangria = Seq(
    "org.sangria-graphql" %% "sangria"           % "1.4.2",
    "org.sangria-graphql" %% "sangria-circe"     % "1.2.1",
    "org.sangria-graphql" %% "sangria-slowlog"   % "0.1.8",
    "com.github.sebruck"  %% "akka-http-graphql" % "0.1.1"
  )

  lazy val apiDependencies = scalatest.map(_ % Test) ++
    akka ++
    akkaHttpCirce ++
    circe ++
    sangria
}
