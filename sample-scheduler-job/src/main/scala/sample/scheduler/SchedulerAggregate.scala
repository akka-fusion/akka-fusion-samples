package sample.scheduler

import akka.Done
import akka.actor.typed.ActorSystem
import akka.cluster.singleton.ClusterSingletonManager
import akka.cluster.singleton.ClusterSingletonManagerSettings
import akka.pattern._
import akka.util.Timeout
import fusion.core.extension.FusionCore
import fusion.core.extension.FusionExtension
import fusion.core.extension.FusionExtensionId
import sample.scheduler.grpc.SchedulerService
import sample.scheduler.model.End
import sample.scheduler.service.SchedulerActor
import sample.scheduler.service.SchedulerServiceImpl

import scala.concurrent.duration._

class SchedulerAggregate private (override val system: ActorSystem[_]) extends FusionExtension {

  // 使用Akka Cluster Singleton保证调度服务Actor在集群中只启动并活跃一个
  private val schedulerActor =
    classicSystem.actorOf(
      ClusterSingletonManager.props(SchedulerActor.props(), End, ClusterSingletonManagerSettings(classicSystem)),
      "sample-scheduler")

  FusionCore(system).shutdowns.serviceRequestsDone("sample-scheduler") { () =>
    // 保存退出时正确的清理调度资源
    implicit val timeout: Timeout = Timeout(60.seconds)
    schedulerActor.ask(End).mapTo[Done].recover { case _ => Done }(system.executionContext)
  }

  val schedulerService: SchedulerService = new SchedulerServiceImpl(classicSystem)
}

object SchedulerAggregate extends FusionExtensionId[SchedulerAggregate] {
  override def createExtension(system: ActorSystem[_]): SchedulerAggregate = new SchedulerAggregate(system)
}
