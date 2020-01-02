import sbt._

object Dependencies {
  val versionScala = "2.13.1"
  val versionScalaLib = "2.13"
  val versionAkka = "2.6.1"
  val versionAkkaFusion = "2.0.1"
  val versionAkkaHttp = "10.1.11"
  val versionJwtCore = "2.1.0"
  val versionMySQL = "8.0.16"
  val versionPostgres = "42.2.9"
  val versionCaffeine = "2.7.0"
  val versionSlickPg = "0.18.0"
  val versionJettyAlpnAgent = "2.0.9"

  val _akkaHttpTestkit = ("com.typesafe.akka" %% "akka-http-testkit" % versionAkkaHttp)
    .excludeAll(ExclusionRule("com.typesafe.akka"))
    .cross(CrossVersion.binary)
  val _fusionHttp = "com.akka-fusion" %% "fusion-http" % versionAkkaFusion
  val _fusionHttpClient = "com.akka-fusion" %% "fusion-http-client" % versionAkkaFusion
  val _fusionHttpGateway = "com.akka-fusion" %% "fusion-http-gateway" % versionAkkaFusion
  val _fusionActuator = "com.akka-fusion" %% "fusion-actuator" % versionAkkaFusion
  val _fusionDiscoveryClient = "com.akka-fusion" %% "fusion-discovery-client" % versionAkkaFusion
  val _fusionSlick = "com.akka-fusion" %% "fusion-slick" % versionAkkaFusion
  val _fusionMongodb = "com.akka-fusion" %% "fusion-mongodb" % versionAkkaFusion
  val _fusionElasticsearch = "com.akka-fusion" %% "fusion-elasticsearch" % versionAkkaFusion
  val _fusionCassandra = "com.akka-fusion" %% "fusion-cassandra" % versionAkkaFusion
  val _fusionKafka = "com.akka-fusion" %% "fusion-kafka" % versionAkkaFusion
  val _fusionJdbc = "com.akka-fusion" %% "fusion-jdbc" % versionAkkaFusion
  val _fusionMail = "com.akka-fusion" %% "fusion-mail" % versionAkkaFusion
  val _fusionJob = "com.akka-fusion" %% "fusion-job" % versionAkkaFusion
  val _fusionJson = "com.akka-fusion" %% "fusion-json" % versionAkkaFusion
  val _fusionLog = "com.akka-fusion" %% "fusion-log" % versionAkkaFusion
  val _fusionCore = "com.akka-fusion" %% "fusion-core" % versionAkkaFusion
  val _fusionCommon = "com.akka-fusion" %% "fusion-common" % versionAkkaFusion
  val _fusionSecurity = "com.akka-fusion" %% "fusion-security" % versionAkkaFusion
  val _fusionTest = "com.akka-fusion" %% "fusion-testkit" % versionAkkaFusion
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
