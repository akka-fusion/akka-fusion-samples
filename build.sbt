import Commons._
import Dependencies._
import Publishing._

ThisBuild / scalaVersion := Dependencies.versionScala

ThisBuild / scalafmtOnCompile := true

ThisBuild / resolvers ++= Seq(
  "hongkazhijia.com sbt-release".at("https://artifactory.hongkazhijia.com/artifactory/sbt-release"),
  "hongkazhijia.com libs-release".at("https://artifactory.hongkazhijia.com/artifactory/libs-release"))

lazy val root =
  Project("akka-fusion-samples", file("."))
    .aggregate(
      `sample-docs`,
      `sample-http-gateway`,
      `sample-discovery`,
      `sample-kafka`,
      `sample-mongodb`,
      `sample-elasticsearch`,
      `sample-slick`,
      `sample-jdbc`,
      `sample-scheduler-job`,
      `sample-grpc`,
      `sample-log`,
      `sample-http-client`,
      `sample-http-server`,
      `sample-common`)
    .settings(noPublish: _*)

lazy val `sample-docs` = _project("sample-docs")
  .dependsOn(
    `sample-http-gateway`,
    `sample-discovery`,
    `sample-kafka`,
    `sample-mongodb`,
    `sample-elasticsearch`,
    `sample-slick`,
    `sample-jdbc`,
    `sample-scheduler-job`,
    `sample-grpc`,
    `sample-log`,
    `sample-http-client`,
    `sample-http-server`,
    `sample-common`)
  .enablePlugins(ParadoxMaterialThemePlugin)
  .settings(noPublish: _*)
  .settings(
    Compile / paradoxMaterialTheme ~= {
      _.withLanguage(java.util.Locale.SIMPLIFIED_CHINESE)
        .withColor("indigo", "red")
        .withRepository(uri("https://github.com/ihongka/akka-fusion"))
        .withSocial(
          uri("http://ihongka.github.io/akka-fusion/"),
          uri("https://github.com/ihongka"),
          uri("https://weibo.com/yangbajing"))
    },
    paradoxProperties ++= Map(
        "github.base_url" -> s"https://github.com/ihongka/akka-fusion-samples/tree/${version.value}",
        "version" -> version.value,
        "scala.version" -> scalaVersion.value,
        "scala.binary_version" -> scalaBinaryVersion.value,
        "scaladoc.akka.base_url" -> s"http://doc.akka.io/api/$versionAkka",
        "akka.version" -> versionAkka))

lazy val `sample-http-gateway` = _project("sample-http-gateway")
  .enablePlugins(JavaAgent)
  .dependsOn(`sample-common`)
  .settings(
    javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test",
    assemblyJarName in assembly := "sample-http-gateway.jar",
    mainClass in assembly := Some("sample.http.gateway.SampleHttpGatewayApplication"),
    libraryDependencies ++= Seq(_fusionHttp, _fusionHttpGateway))

lazy val `sample-mongodb` =
  _project("sample-mongodb").dependsOn(`sample-common`).settings(libraryDependencies += _fusionMongodb)

lazy val `sample-kafka` =
  _project("sample-kafka").dependsOn(`sample-common`).settings(libraryDependencies += _fusionKafka)

lazy val `sample-elasticsearch` =
  _project("sample-elasticsearch").dependsOn(`sample-common`).settings(libraryDependencies += _fusionElasticsearch)

lazy val `sample-discovery` =
  _project("sample-discovery")
    .dependsOn(`sample-common`)
    .settings(libraryDependencies ++= Seq(_fusionDiscoveryClient, _akkaHttpTestkit % Test))

lazy val `sample-slick` =
  _project("sample-slick")
    .dependsOn(`sample-jdbc`, `sample-common`)
    .settings(libraryDependencies ++= Seq(_postgresql, _fusionSlick))

lazy val `sample-jdbc` =
  _project("sample-jdbc").dependsOn(`sample-common`).settings(libraryDependencies ++= Seq(_fusionJdbc, _h2Database))

lazy val `sample-scheduler-job` = _project("sample-scheduler-job")
  .dependsOn(`sample-common`)
  .enablePlugins(AkkaGrpcPlugin, JavaAgent)
  .settings(
    PB.protocVersion := "-v371",
    javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test",
    assemblyJarName in assembly := "sample-scheduler.jar",
    mainClass in assembly := Some("sample.scheduler.SampleSchedulerJobApplication"),
    libraryDependencies ++= Seq(
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
        _postgresql,
        _fusionHttp,
        _fusionJob,
        _akkaHttpTestkit % Test) ++ _akkaClusters)

lazy val `sample-grpc` = _project("sample-grpc")
  .dependsOn(`sample-common`)
  .enablePlugins(AkkaGrpcPlugin, JavaAgent)
  .settings(
    PB.protocVersion := "-v371",
    akkaGrpcCodeGeneratorSettings += "server_power_apis",
    javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test",
    assemblyJarName in assembly := "sample-grpc.jar",
    mainClass in assembly := Some("sample.grpc.GrpcApplication"),
    libraryDependencies ++= Seq(
        "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
        _fusionHttp,
        _akkaDiscovery))

lazy val `sample-log` =
  _project("sample-log").dependsOn(`sample-common`).settings(libraryDependencies ++= Seq(_fusionLog, _fusionHttp))

lazy val `sample-http-client` =
  _project("sample-http-client")
    .dependsOn(`sample-common`)
    .settings(libraryDependencies ++= Seq(_fusionHttpClient, _fusionHttp))

lazy val `sample-http-server` = _project("sample-http-server")
  .enablePlugins(JavaAgent)
  .dependsOn(`sample-common`)
  .settings(
    javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test",
    assemblyJarName in assembly := "sample-http-server.jar",
    mainClass in assembly := Some("sample.http.server.SampleHttpServerApplication"),
    libraryDependencies ++= Seq(_fusionHttp))

lazy val `sample-common` =
  _project("sample-common")
    .enablePlugins(AkkaGrpcPlugin)
    .settings(publishing: _*)
    .settings(
      PB.protocVersion := "-v371",
      libraryDependencies ++= Seq(
          "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
          _fusionSecurity,
          _fusionJson,
          _fusionCommon))

def _project(name: String, _base: String = null) =
  Project(id = name, base = file(if (_base eq null) name else _base))
    .settings(basicSettings: _*)
    .settings(noPublish: _*)
    .settings(libraryDependencies ++= Seq(_fusionTest % Test))
