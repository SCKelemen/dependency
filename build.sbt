name := "dependency"

organization := "io.flow"

scalaVersion in ThisBuild := "2.13.1"

lazy val generated = project
  .in(file("generated"))
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= Seq(
      ws,
      compilerPlugin("com.github.ghik" %% "silencer-plugin" % "1.4.4" cross CrossVersion.full),
      "com.github.ghik" %% "silencer-lib" % "1.4.4" % Provided cross CrossVersion.full
    ),
    // silence all warnings on autogenerated files
    flowGeneratedFiles ++= Seq(
      "app/.*".r,
    ),
    // Make sure you only exclude warnings for the project directories, i.e. make builds reproducible
    scalacOptions += s"-P:silencer:sourceRoots=${baseDirectory.value.getCanonicalPath}",
  )

lazy val lib = project
  .in(file("lib"))
  .dependsOn(generated)
  .aggregate(generated)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-test" % "2.8.1",
      "com.typesafe.play" %% "play-specs2" % "2.8.1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0",
      "org.specs2" %% "specs2-core" % "4.8.3",
    )
  )

lazy val api = project
  .in(file("api"))
  .dependsOn(generated, lib)
  .aggregate(generated, lib)
  .enablePlugins(PlayScala)
  .enablePlugins(NewRelic)
  .enablePlugins(JavaAppPackaging, JavaAgent)
  .settings(commonSettings: _*)
  .settings(
    javaAgents += "io.kamon" % "kanela-agent" % "1.0.4",
    routesImport += "io.flow.dependency.v0.Bindables.Core._",
    routesImport += "io.flow.dependency.v0.Bindables.Models._",
    routesGenerator := InjectedRoutesGenerator,
    libraryDependencies ++= Seq(
      jdbc,
      ws,
      guice,
      "com.sendgrid" % "sendgrid-java" % "4.4.1",
      "io.flow" %% "lib-util" % "0.1.37",
      "io.flow" %% "lib-akka-akka26" % "0.1.14",
      "io.flow" %% "lib-postgresql-play28" % "0.1.44",
      "io.flow" %% "lib-play-graphite-play28" % "0.1.36",
      "io.flow" %% "lib-log" % "0.0.97",
      "io.flow" %% "lib-usage-play28" % "0.1.14",
      "io.flow" %% "lib-test-utils-play28" % "0.0.81" % Test,
      "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.23",
      "org.postgresql" % "postgresql" % "42.2.9",
      "org.apache.commons" % "commons-text" % "1.8",
      compilerPlugin("com.github.ghik" %% "silencer-plugin" % "1.4.4" cross CrossVersion.full),
      "com.github.ghik" %% "silencer-lib" % "1.4.4" % Provided cross CrossVersion.full
    ),
    // silence all warnings on autogenerated files
    flowGeneratedFiles ++= Seq(
      "target/*".r,
      "app/generated/.*".r,
      "app/db/generated/.*".r,
    ),
    // Make sure you only exclude warnings for the project directories, i.e. make builds reproducible
    scalacOptions += s"-P:silencer:sourceRoots=${baseDirectory.value.getCanonicalPath}",
  )

lazy val www = project
  .in(file("www"))
  .dependsOn(generated, lib)
  .aggregate(generated, lib)
  .enablePlugins(PlayScala)
  .enablePlugins(NewRelic)
  .enablePlugins(SbtWeb)
  .settings(commonSettings: _*)
  .settings(
    routesImport += "io.flow.dependency.v0.Bindables.Core._",
    routesImport += "io.flow.dependency.v0.Bindables.Models._",
    routesGenerator := InjectedRoutesGenerator,
    libraryDependencies ++= Seq(
      ws,
      guice,
      "org.webjars" %% "webjars-play" % "2.8.0",
      "org.webjars" % "bootstrap" % "3.4.1",
      "org.webjars" % "font-awesome" % "5.11.2",
      "org.webjars" % "jquery" % "2.1.4",
      "org.webjars.bower" % "bootstrap-social" % "5.1.1",
      "io.flow" %% "lib-test-utils-play28" % "0.0.81" % Test,
      compilerPlugin("com.github.ghik" %% "silencer-plugin" % "1.4.4" cross CrossVersion.full),
      "com.github.ghik" %% "silencer-lib" % "1.4.4" % Provided cross CrossVersion.full
    ),
    // silence all warnings on autogenerated files
    flowGeneratedFiles ++= Seq(
      "target/*".r,
    ),
    // Make sure you only exclude warnings for the project directories, i.e. make builds reproducible
    scalacOptions += s"-P:silencer:sourceRoots=${baseDirectory.value.getCanonicalPath}",
  )

val credsToUse = Option(System.getenv("ARTIFACTORY_USERNAME")) match {
  case None => Credentials(Path.userHome / ".ivy2" / ".artifactory")
  case _ => Credentials("Artifactory Realm","flow.jfrog.io",System.getenv("ARTIFACTORY_USERNAME"),System.getenv("ARTIFACTORY_PASSWORD"))
}

lazy val commonSettings: Seq[Setting[_]] = Seq(
  name ~= ("dependency-" + _),
  libraryDependencies ++= Seq(
    "io.flow" %% "lib-play-play28" % "0.5.91",
    "com.typesafe.play" %% "play-json-joda" % "2.8.1",
    "com.typesafe.play" %% "play-json" % "2.8.1"
  ),
  scalacOptions += "-feature",
  credentials += credsToUse,
  resolvers += "Artifactory" at "https://flow.jfrog.io/flow/libs-release/"
)
version := "0.7.26"
