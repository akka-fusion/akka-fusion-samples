package sample.grpc

import akka.actor.ActorSystem
import fusion.http.FusionHttpServer

object SampleGrpcApplication {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    FusionHttpServer(system).component.startAbstractRouteSync(new Routes())
  }
}
