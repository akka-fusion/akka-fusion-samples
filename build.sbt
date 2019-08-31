import Commons._
import Publishing._
import Dependencies._

scalaVersion in ThisBuild := Dependencies.versionScala

scalafmtOnCompile in ThisBuild := true

lazy val root =
  Project("akka-fusion-samples", file("."))
    .aggregate(
      `sample-docs`,
      `sample-http-gateway`,
      `sample-discovery`,
      `sample-slick`,
      `sample-jdbc`,
      `sample-scheduler-job`,
      `sample-log`,
      `sample-http-client`,
      `sample-http-server`,
      `sample-common`)
    .settings(noPublish: _*)

lazy val `sample-docs` = _project("sample-docs")
  .aggregate(
    `sample-http-gateway`,
    `sample-discovery`,
    `sample-slick`,
    `sample-jdbc`,
    `sample-scheduler-job`,
    `sample-log`,
    `sample-http-client`,
    `sample-http-server`,
    `sample-common`)
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
      "github.base_url" -> s"https://github.com/ihongka/akka-fusion/tree/${version.value}",
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

lazy val `sample-discovery` =
  _project("sample-discovery").dependsOn(`sample-common`).settings(libraryDependencies ++= Seq(_fusionDiscoveryClient))

lazy val `sample-slick` =
  _project("sample-slick").dependsOn(`sample-jdbc`, `sample-common`).settings(libraryDependencies ++= Seq(_postgresql))

lazy val `sample-jdbc` =
  _project("sample-jdbc").dependsOn(`sample-common`).settings(libraryDependencies ++= Seq(_fusionJdbc, _h2Database))

lazy val `sample-scheduler-job` =
  _project("sample-scheduler-job")
    .dependsOn(`sample-common`)
    .settings(
      assemblyJarName in assembly := "sample-scheduler-job.jar",
      mainClass in assembly := Some("sample.scheduler.job.SampleSchedulerJobApplication"),
      libraryDependencies ++= Seq(_fusionJob) ++ _akkaClusters)

lazy val `sample-log` =
  _project("sample-log")
    .dependsOn(`sample-common`)
    .settings(libraryDependencies ++= Seq(_fusionLog, _fusionHttp))

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
    .settings(publishing: _*)
    .settings(libraryDependencies ++= Seq(_fusionSecurity, _fusionJsonCirce, _fusionCommon))

def _project(name: String, _base: String = null) =
  Project(id = name, base = file(if (_base eq null) name else _base))
    .settings(basicSettings: _*)
    .settings(noPublish: _*)
    .settings(libraryDependencies ++= Seq(_fusionTest % Test))
