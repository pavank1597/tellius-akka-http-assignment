name := "tellius-akkahttp-assignment"

version := "0.1"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.20"
val akkaHttpVersion = "10.1.7"
val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  // akka streams
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  // akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  // testing
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.pauldijou" %% "jwt-spray-json" % "2.1.0",
  "org.scalatest" %% "scalatest" % scalaTestVersion,
  "com.typesafe.akka" %% "akka-persistence-jdbc" % "3.5.3",
  "com.typesafe.akka" %% "akka-stream-alpakka-jdbc" % "3.0.1",
  "org.postgresql" % "postgresql" % "42.6.0"






//org.postgresql.ds.PGSimpleDataSource dependency

)