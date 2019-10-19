import sbt._

object Dependencies {
  val versionScala = "2.13.1"
  val versionScalaLib = "2.13"
  val versionAkka = "2.6.0-RC1"
  val versionAkkaFusion = "2.0.0-SNAPSHOT"
  val versionAkkaHttp = "10.1.10"
  val versionJwtCore = "2.1.0"
  val versionMySQL = "8.0.16"
  val versionPostgres = "42.2.8"
  val versionCaffeine = "2.7.0"
  val versionSlickPg = "0.18.0"
  val versionJettyAlpnAgent = "2.0.9"

  val _akkaHttpTestkit = ("com.typesafe.akka" %% "akka-http-testkit" % versionAkkaHttp)
    .excludeAll(ExclusionRule("com.typesafe.akka"))
    .cross(CrossVersion.binary)
  val _fusionHttp = "com.helloscala.fusion" %% "fusion-http" % versionAkkaFusion
  val _fusionHttpClient = "com.helloscala.fusion" %% "fusion-http-client" % versionAkkaFusion
  val _fusionHttpGateway = "com.helloscala.fusion" %% "fusion-http-gateway" % versionAkkaFusion
  val _fusionActuator = "com.helloscala.fusion" %% "fusion-actuator" % versionAkkaFusion
  val _fusionDiscoveryClient = "com.helloscala.fusion" %% "fusion-discovery-client" % versionAkkaFusion
  val _fusionSlick = "com.helloscala.fusion" %% "fusion-slick" % versionAkkaFusion
  val _fusionMongodb = "com.helloscala.fusion" %% "fusion-mongodb" % versionAkkaFusion
  val _fusionElasticsearch = "com.helloscala.fusion" %% "fusion-elasticsearch" % versionAkkaFusion
  val _fusionCassandra = "com.helloscala.fusion" %% "fusion-cassandra" % versionAkkaFusion
  val _fusionKafka = "com.helloscala.fusion" %% "fusion-kafka" % versionAkkaFusion
  val _fusionJdbc = "com.helloscala.fusion" %% "fusion-jdbc" % versionAkkaFusion
  val _fusionMail = "com.helloscala.fusion" %% "fusion-mail" % versionAkkaFusion
  val _fusionJob = "com.helloscala.fusion" %% "fusion-job" % versionAkkaFusion
  val _fusionJson = "com.helloscala.fusion" %% "fusion-json" % versionAkkaFusion
  val _fusionLog = "com.helloscala.fusion" %% "fusion-log" % versionAkkaFusion
  val _fusionCore = "com.helloscala.fusion" %% "fusion-core" % versionAkkaFusion
  val _fusionCommon = "com.helloscala.fusion" %% "fusion-common" % versionAkkaFusion
  val _fusionSecurity = "com.helloscala.fusion" %% "fusion-security" % versionAkkaFusion
  val _fusionTest = "com.helloscala.fusion" %% "fusion-test" % versionAkkaFusion
  val _jwtCore = "com.pauldijou" %% "jwt-core" % versionJwtCore
  val _postgresql = "org.postgresql" % "postgresql" % versionPostgres
  val _h2Database = "com.h2database" % "h2" % "1.4.199"
  val _akkaDiscovery = "com.typesafe.akka" %% "akka-discovery" % versionAkka

  val _akkaClusters =
    Seq(
      "com.typesafe.akka" %% "akka-cluster-typed" % versionAkka,
      "com.typesafe.akka" %% "akka-cluster-tools" % versionAkka,
      _akkaDiscovery)
}
