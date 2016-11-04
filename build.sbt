name := "HackerRank-problems"

version := "0.1"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-deprecation")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.15",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.15",
  "com.typesafe.akka" %% "akka-contrib" % "2.3.15",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.3.15",
  "com.typesafe.akka" %% "akka-remote" % "2.3.15",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.15",
  "org.slf4j" % "slf4j-simple" % "1.6.1",
  "org.slf4j" % "slf4j-api" % "1.6.1"
)

resolvers ++= Seq(
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Scala-Tools Snapshots" at "http://scala-tools.org/repo-snapshots",
  "Scala Tools Releases" at "http://scala-tools.org/repo-releases"
)
