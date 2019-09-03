package sample.scheduler

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import fusion.http.server.AbstractRoute
import sample.scheduler.grpc.SchedulerServiceHandler
import sample.scheduler.route.SchedulerRoute

class Routes()(implicit system: ActorSystem) extends AbstractRoute {
  implicit private val mat = ActorMaterializer()
  private val aggregate = SchedulerAggregate(system)
  private val grpcHandler = SchedulerServiceHandler(aggregate.schedulerService)

  override def route: Route = {
    pathPrefix("api" / "v4") {
      new SchedulerRoute(system).route
    } ~
    grpcRoute
  }

  def grpcRoute: Route = extractRequest { request =>
    onSuccess(grpcHandler(request)) { response =>
      complete(response)
    }
  }

}
