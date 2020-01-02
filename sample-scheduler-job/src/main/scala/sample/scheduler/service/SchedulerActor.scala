package sample.scheduler.service

import akka.Done
import akka.actor.{ Actor, ActorLogging, Props }
import akka.actor.typed.scaladsl.adapter._
import fusion.job.{ FusionJob, FusionScheduler }
import sample.scheduler.model._

/**
 * TODO 待重构，若参考 fusion-schedulerx
 */
class SchedulerActor extends Actor with SchedulerServiceComponent with ActorLogging {
  override def preStart(): Unit = {
    super.preStart()
    context.become(onMessage(FusionJob(context.system.toTyped).component))
    log.info("Scheduler actor startup.")
  }

  override def postStop(): Unit = {
    super.postStop()
    log.info("Scheduler actor stopped.")
  }

  override def receive: Receive = {
    case End =>
      sender() ! Done
      context.stop(self)
    case other =>
      log.warning(s"Scheduler actor not startup, receive message is $other")
      sender() ! Done
  }

  private def onMessage(implicit scheduler: FusionScheduler): Receive = {
    case dto: JobCancelDTO => sender() ! cancelJob(dto)
    case dto: JobDTO       => sender() ! createJob(dto)
    case dto: JobGetDTO    => sender() ! getJob(dto)
    case dto: JobPauseDTO  => sender() ! pauseJob(dto)
    case dto: JobResumeDTO => sender() ! resumeJob(dto)
    case End =>
      scheduler.close()
      sender() ! Done
      context.stop(self)
  }
}

object SchedulerActor {
  def props(): Props = Props(new SchedulerActor)
}
