package sample.scheduler.service

import akka.cluster.singleton.ClusterSingletonProxy
import akka.cluster.singleton.ClusterSingletonProxySettings
import akka.pattern._
import akka.util.Timeout
import akka.{actor => classic}
import sample.ResultBO
import sample.scheduler.grpc.SchedulerService
import sample.scheduler.model._

import scala.concurrent.Future
import scala.concurrent.duration._

class SchedulerServiceImpl(system: classic.ActorSystem) extends SchedulerService {
  implicit private val timeout = Timeout(10.seconds)

  private val proxy =
    system.actorOf(ClusterSingletonProxy.props("/user/sample-scheduler", ClusterSingletonProxySettings(system)))

  override def cancelJob(dto: JobCancelDTO): Future[ResultBO] = proxy.ask(dto).mapTo[ResultBO]

  override def createJob(dto: JobDTO): Future[JobBO] = proxy.ask(dto).mapTo[JobBO]

  override def getJob(in: Key): Future[JobBO] = proxy.ask(JobGetDTO(Some(in))).mapTo[JobBO]

  override def pauseJob(in: JobPauseDTO): Future[ResultBO] = proxy.ask(in).mapTo[ResultBO]

  override def resumeJob(in: JobResumeDTO): Future[ResultBO] = proxy.ask(in).mapTo[ResultBO]

}
