import Commons._
import Publishing._
import Dependencies._

scalaVersion in ThisBuild := Dependencies.versionScala

scalafmtOnCompile in ThisBuild := true

lazy val root =
  Project("akka-fusion-samples", file(".")).aggregate(`sample-http-gateway`, `sample-common`).settings(noPublish: _*)

lazy val `sample-http-gateway` = _project("sample-http-gateway")
  .enablePlugins(JavaAgent)
  .dependsOn(`sample-common`)
  .settings(
    Protobufs.protocVersion,
    javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test",
    assemblyJarName in assembly := "sample-http-gateway.jar",
    mainClass in assembly := Some("sample.http.gateway.SampleHttpGatewayApplication"),
    libraryDependencies ++= Seq(_fusionHttp, _fusionHttpGateway))

lazy val `sample-common` =
  _project("sample-common")
    .settings(publishing: _*)
    .settings(libraryDependencies ++= Seq(_akkaHttpCirce, _fusionSecurity, _fusionJsonCirce, _fusionCommon))

def _project(name: String, _base: String = null) =
  Project(id = name, base = file(if (_base eq null) name else _base))
    .settings(basicSettings: _*)
    .settings(noPublish: _*)
    .settings(libraryDependencies ++= Seq(_fusionTest % Test))
