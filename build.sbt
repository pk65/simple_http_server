val scala3Version = "3.1.0"
val http4sVersion = "0.23.6"

lazy val root = project
  .in(file("."))
  .settings(
    name := "simple_http_server",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "com.novocode" % "junit-interface" % "0.11" % "test"
    )
  )
