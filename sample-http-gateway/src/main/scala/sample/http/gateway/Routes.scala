package sample.http.gateway

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Route
import fusion.http.gateway.server.FusionHttpGateway
import fusion.http.server.AbstractRoute

class Routes(system: ActorSystem[_]) extends AbstractRoute {
  private val gatewayRoute = FusionHttpGateway(system).component.route
  def route: Route = gatewayRoute
}
