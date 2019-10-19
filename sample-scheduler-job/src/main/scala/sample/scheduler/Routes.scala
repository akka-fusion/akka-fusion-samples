package sample.scheduler

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import fusion.core.extension.FusionCore
import fusion.http.server.AbstractRoute
import sample.scheduler.grpc.SchedulerServiceHandler
import sample.scheduler.route.SchedulerRoute

class Routes(system: ActorSystem[_]) extends AbstractRoute {
  implicit private val mat = Materializer(system)
  implicit private val classicSystem = FusionCore(system).classicSystem
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
