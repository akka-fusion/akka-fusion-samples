package sample.scheduler.route

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Route
import fusion.http.server.AbstractRoute
import sample.scheduler.SchedulerAggregate
import sample.scheduler.model._

class SchedulerRoute(system: ActorSystem[_]) extends AbstractRoute {
  private val schedulerService = SchedulerAggregate(system).schedulerService

  override def route: Route = pathPrefix("scheduler") {
    getJobRoute ~
    createJobRoute ~
    pauseJobRoute ~
    resumeJobRoute ~
    cancelJobRoute
  }

  import fusion.json.json4s.http.Json4sSupport._

  def createJobRoute: Route = pathPost("create") {
    entity(as[JobDTO]) { dto =>
      complete(schedulerService.createJob(dto))
    }
  }

  def pauseJobRoute: Route = pathPost("pause") {
    entity(as[JobPauseDTO]) { dto =>
      complete(schedulerService.pauseJob(dto))
    }
  }

  def resumeJobRoute: Route = pathPost("resume") {
    entity(as[JobResumeDTO]) { dto =>
      complete(schedulerService.resumeJob(dto))
    }
  }

  def cancelJobRoute: Route = pathPost("cancel") {
    entity(as[JobCancelDTO]) { dto =>
      complete(schedulerService.cancelJob(dto))
    }
  }

  def getJobRoute: Route = pathGet("item") {
    parameters(('name, 'group)) { (name, group) =>
      complete(schedulerService.getJob(Key(group, name)))
    }
  }
}
