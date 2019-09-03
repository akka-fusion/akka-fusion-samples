package sample.scheduler.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import fusion.http.server.AbstractRoute
import sample.scheduler.SchedulerAggregate
import sample.scheduler.model.JobCancelDTO
import sample.scheduler.model.JobDTO
import sample.scheduler.model.JobPauseDTO
import sample.scheduler.model.JobResumeDTO
import sample.scheduler.model.Key

class SchedulerRoute(system: ActorSystem) extends AbstractRoute {
  private val schedulerService = SchedulerAggregate(system).schedulerService

  override def route: Route = pathPrefix("scheduler") {
    getJobRoute ~
    createJobRoute ~
    pauseJobRoute ~
    resumeJobRoute ~
    cancelJobRoute
  }

  import sample.scheduler.util.CirceSupport._

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
