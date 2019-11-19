name := "akka-http-unit-test"
organization := "com.amdelamar"
version := "1.0"
scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.19",
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.8" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.19" % Test,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.scalamock" %% "scalamock" % "4.1.0" % Test
)

lazy val root = (project in file("."))
