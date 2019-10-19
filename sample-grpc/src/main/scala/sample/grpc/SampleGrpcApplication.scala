package sample.grpc

import com.typesafe.config.ConfigFactory
import fusion.core.util.FusionUtils
import fusion.http.FusionHttpServer

object SampleGrpcApplication {

  def main(args: Array[String]): Unit = {
    implicit val system = FusionUtils.createActorSystem(ConfigFactory.load())
    FusionHttpServer(system).component.startAbstractRouteSync(new Routes())
  }
}
