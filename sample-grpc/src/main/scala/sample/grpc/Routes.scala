package sample.grpc

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import fusion.http.server.AbstractRoute

class Routes()(implicit system: ActorSystem) extends AbstractRoute with StrictLogging {
  private val grpcHandlers = SampleGrpcAggregate(system).grpcHandlers
    .andThen(f =>
      onSuccess(f) { response =>
        complete(response)
      })
    .orElse[HttpRequest, Route] {
      case req =>
        logger.warn(s"gRPC Handler not exists. $req")
        reject
    }

  override def route: Route = grpcRoute

  def grpcRoute: Route = extractRequest { request =>
    grpcHandlers(request)
  }
}
