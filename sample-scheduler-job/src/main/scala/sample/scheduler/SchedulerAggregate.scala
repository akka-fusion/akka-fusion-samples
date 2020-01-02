package sample.scheduler

import akka.Done
import akka.actor.typed.ActorSystem
import akka.cluster.singleton.{ ClusterSingletonManager, ClusterSingletonManagerSettings }
import akka.pattern._
import akka.util.Timeout
import fusion.common.extension.{ FusionExtension, FusionExtensionId }
import fusion.core.extension.FusionCore
import sample.scheduler.grpc.SchedulerService
import sample.scheduler.model.End
import sample.scheduler.service.{ SchedulerActor, SchedulerServiceImpl }

import scala.concurrent.duration._

class SchedulerAggregate private (override val system: ActorSystem[_]) extends FusionExtension {
  // 使用Akka Cluster Singleton保证调度服务Actor在集群中只启动并活跃一个
  private val schedulerActor =
    classicSystem.actorOf(
      ClusterSingletonManager.props(SchedulerActor.props(), End, ClusterSingletonManagerSettings(classicSystem)),
      "sample-scheduler")

  FusionCore(system).shutdowns.serviceRequestsDone("sample-scheduler") { () =>
    implicit val timeout: Timeout = 60.seconds
    schedulerActor.ask(End).mapTo[Done].recover { case _ => Done }(system.executionContext)
  }

  val schedulerService: SchedulerService = new SchedulerServiceImpl(classicSystem)
}

object SchedulerAggregate extends FusionExtensionId[SchedulerAggregate] {
  override def createExtension(system: ActorSystem[_]): SchedulerAggregate = new SchedulerAggregate(system)
}
