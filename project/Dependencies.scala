import sbt._

object Dependencies {
  val versionScala           = "2.12.9"
  val versionScalaLib        = "2.12"
  val versionAkka            = "2.5.25"
  val versionAkkaFusion      = "1.0.0-SNAPSHOT"
  val versionJwtCore         = "2.1.0"
  val versionMySQL           = "8.0.16"
  val versionPostgres        = "42.2.5"
  val versionCaffeine        = "2.7.0"
  val versionSlickPg         = "0.18.0"
  val versionJettyAlpnAgent  = "2.0.9"
  val _fusionHttp            = "com.helloscala.fusion" %% "fusion-http" % versionAkkaFusion
  val _fusionHttpGateway     = "com.helloscala.fusion" %% "fusion-http-gateway" % versionAkkaFusion
  val _fusionActuator        = "com.helloscala.fusion" %% "fusion-actuator" % versionAkkaFusion
  val _fusionDiscoveryClient = "com.helloscala.fusion" %% "fusion-discovery-client" % versionAkkaFusion
  val _fusionSlick           = "com.helloscala.fusion" %% "fusion-slick" % versionAkkaFusion
  val _fusionMongodb         = "com.helloscala.fusion" %% "fusion-mongodb" % versionAkkaFusion
  val _fusionCassandra       = "com.helloscala.fusion" %% "fusion-cassandra" % versionAkkaFusion
  val _fusionKafka           = "com.helloscala.fusion" %% "fusion-kafka" % versionAkkaFusion
  val _fusionJdbc            = "com.helloscala.fusion" %% "fusion-jdbc" % versionAkkaFusion
  val _fusionMail            = "com.helloscala.fusion" %% "fusion-mail" % versionAkkaFusion
  val _fusionJob             = "com.helloscala.fusion" %% "fusion-job" % versionAkkaFusion
  val _fusionJsonCirce       = "com.helloscala.fusion" %% "fusion-json-circe" % versionAkkaFusion
  val _fusionLog             = "com.helloscala.fusion" %% "fusion-log" % versionAkkaFusion
  val _fusionCore            = "com.helloscala.fusion" %% "fusion-core" % versionAkkaFusion
  val _fusionCommon          = "com.helloscala.fusion" %% "fusion-common" % versionAkkaFusion
  val _fusionSecurity        = "com.helloscala.fusion" %% "fusion-security" % versionAkkaFusion
  val _fusionTest            = "com.helloscala.fusion" %% "fusion-test" % versionAkkaFusion
  val _slickPg               = "com.github.tminglei" %% "slick-pg" % versionSlickPg
  val _jwtCore               = "com.pauldijou" %% "jwt-core" % versionJwtCore
  val _postgresql            = "org.postgresql" % "postgresql" % versionPostgres
  val _mysql                 = "mysql" % "mysql-connector-java" % versionMySQL
  val _caffeine              = "com.github.ben-manes.caffeine" % "caffeine" % versionCaffeine
  val _hanlp                 = "com.hankcs" % "hanlp" % "portable-1.7.4"
  val _akkaGrpcRuntime       = "com.lightbend.akka.grpc" %% "akka-grpc-runtime" % "0.7.0"
  val _akkaHttpCirce         = "de.heikoseeberger" %% "akka-http-circe" % "1.27.0"
}