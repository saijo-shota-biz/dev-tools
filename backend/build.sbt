name := """backend"""
organization := "com.systemya-saijo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

compile := ((Compile / compile) dependsOn Def.task {
  (Compile / sourceManaged).value.mkdirs()
}).value
Compile / javacOptions ++= Seq("-s", s"${sourceManaged.value}/main")

libraryDependencies ++= Seq(
  guice,
  // database
  javaJdbc,
  "org.postgresql" % "postgresql" % "42.2.23",
  // validation
  "org.glassfish" % "javax.el" % "3.0.1-b10",
  "javax.validation" % "validation-api" % "2.0.1.Final",
  "commons-validator" % "commons-validator" % "1.7",
// lombok
  "org.projectlombok" % "lombok" % "1.18.20" % "provided",
  // doma2
  "org.seasar.doma" % "doma-core" % "2.47.1" % "runtime",
  "org.seasar.doma" % "doma-slf4j" % "2.47.1",
  "org.seasar.doma" % "doma-processor" % "2.47.1" % "provided",
  // swagger
  "com.github.dwickern" %% "swagger-play2.8" % "3.1.0",
  "io.swagger" % "swagger-core" % "1.6.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.4",
  // test
  "org.scalactic" %% "scalactic" % "3.2.9",
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "org.mockito" % "mockito-core" % "3.11.2" % Test,
  "org.mockito" % "mockito-inline" % "3.11.2" % Test,
  "org.dbunit" % "dbunit" % "2.7.2" % Test,
  "com.vladsch.flexmark" % "flexmark-all" % "0.35.10" % Test,
  "org.apache.poi" % "poi" % "5.0.0" % Test,
  "org.apache.poi" % "poi-ooxml" % "5.0.0" % Test,
)

libraryDependencies += "org.webjars" % "swagger-ui" % "3.51.1"

Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")

PlayKeys.externalizeResources := false

// https://qiita.com/Eustace/items/2589f7f102d54ec7111a
PlayKeys.fileWatchService := play.dev.filewatch.FileWatchService.jdk7(play.sbt.run.toLoggerProxy(sLog.value))

fork := true // required for "sbt run" to pick up javaOptions
javaOptions += "-Dplay.editor=http://localhost:63342/api/file/?file=%s&line=%s"