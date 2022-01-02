import sbt.Keys.testFrameworks

val scala3Version = "3.1.0"
val http4sVersion = "0.23.7"
val LogbackVersion = "1.2.10"
val CirceVersion = "0.15.0-M1"
val MunitCatsEffectVersion = "1.0.7"

lazy val root = project
  .in(file("."))
  .settings(
    name := "simple_http_server",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    scalacOptions ++= {
      Seq(
        // "-encoding", "UTF-8",
        "-feature",
        "-language:implicitConversions",
        // "-Ylog-classpath",
        "-new-syntax",
        "-explain", "explain-types",
        // "-rewrite",
        "-source:future-migration",
        "-Xfatal-warnings",
        "-Yexplicit-nulls",
        "-deprecation"
      )
    },

    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe"        % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "ch.qos.logback"   % "logback-classic"     % LogbackVersion,
      "org.http4s"      %% "http4s-core" % http4sVersion classifier "javadoc",
      "io.circe"        %% "circe-core" % "0.14.1" classifier "javadoc",
      "co.fs2"          %% "fs2-core" % "3.2.4" classifier "javadoc",
      "org.typelevel" %% "cats-effect" % "3.3.0" classifier "javadoc",
      "io.circe"        %% "circe-generic"       % CirceVersion classifier "javadoc",
      "org.typelevel"   %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test
    ),

    testFrameworks += new TestFramework("munit.Framework")
  )
