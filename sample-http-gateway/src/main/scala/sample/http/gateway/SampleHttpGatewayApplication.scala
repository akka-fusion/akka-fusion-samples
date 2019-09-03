package sample.http.gateway

import fusion.core.util.FusionUtils
import fusion.http.FusionHttpServer
import helloscala.common.Configuration

object SampleHttpGatewayApplication {

  def main(args: Array[String]): Unit = {
    val configuration = Configuration.fromDiscovery()
    val system = FusionUtils.createActorSystem(configuration)
    val route = new Routes(system).route
    FusionHttpServer(system).component.startRouteSync(route)
  }
}
