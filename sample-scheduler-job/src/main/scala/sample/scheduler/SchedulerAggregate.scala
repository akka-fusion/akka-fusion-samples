package sample.scheduler

import akka.Done
import akka.actor.ExtendedActorSystem
import akka.actor.Extension
import akka.actor.ExtensionId
import akka.actor.ExtensionIdProvider
import akka.cluster.singleton.ClusterSingletonManager
import akka.cluster.singleton.ClusterSingletonManagerSettings
import akka.pattern._
import akka.util.Timeout
import fusion.core.extension.FusionCore
import fusion.core.extension.FusionExtension
import sample.scheduler.grpc.SchedulerService
import sample.scheduler.model.End
import sample.scheduler.service.SchedulerActor
import sample.scheduler.service.SchedulerServiceImpl

import scala.concurrent.duration._

class SchedulerAggregate private (protected val _system: ExtendedActorSystem) extends FusionExtension {
  // 使用Akka Cluster Singleton保证调度服务Actor在集群中只启动并活跃一个
  private val schedulerActor =
    system.actorOf(
      ClusterSingletonManager.props(SchedulerActor.props(), End, ClusterSingletonManagerSettings(system)),
      "sample-scheduler")

  FusionCore(system).shutdowns.serviceRequestsDone("sample-scheduler") { () =>
    // 保存退出时正确的清理调度资源
    implicit val timeout: Timeout = Timeout(60.seconds)
    schedulerActor.ask(End).mapTo[Done].recover { case _ => Done }(system.dispatcher)
  }

  val schedulerService: SchedulerService = new SchedulerServiceImpl(system)
}

object SchedulerAggregate extends ExtensionId[SchedulerAggregate] with ExtensionIdProvider {
  override def createExtension(system: ExtendedActorSystem): SchedulerAggregate = new SchedulerAggregate(system)
  override def lookup(): ExtensionId[_ <: Extension] = SchedulerAggregate
}
