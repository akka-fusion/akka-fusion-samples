package sample.http.gateway

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import fusion.http.gateway.server.FusionHttpGateway
import fusion.http.server.AbstractRoute

class Routes(system: ActorSystem) extends AbstractRoute {
  private val gatewayRoute = FusionHttpGateway(system).component
  def route: Route         = gatewayRoute
}
