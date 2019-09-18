package sample.grpc

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import fusion.http.server.AbstractRoute
import fusion.http.server.GrpcUtils

class Routes()(implicit system: ActorSystem) extends AbstractRoute with StrictLogging {

  private val grpcHandlers = GrpcUtils.contactToRoute(SampleGrpcAggregate(system).grpcHandlers: _*)

  override def route: Route = grpcRoute

  def grpcRoute: Route = extractRequest { request =>
    grpcHandlers(request)
  }
}
