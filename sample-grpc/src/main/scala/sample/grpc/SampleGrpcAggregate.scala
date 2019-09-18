package sample.grpc

import akka.actor.ExtendedActorSystem
import akka.actor.Extension
import akka.actor.ExtensionId
import akka.actor.ExtensionIdProvider
import akka.stream.ActorMaterializer
import fusion.core.extension.FusionExtension
import sample.HelloServicePowerApi
import sample.HelloServicePowerApiHandler
import sample.grpc.service.HelloServiceImpl

class SampleGrpcAggregate(protected val _system: ExtendedActorSystem) extends FusionExtension {
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val helloService: HelloServicePowerApi = new HelloServiceImpl()

  val grpcHandlers = List(HelloServicePowerApiHandler.partial(helloService))
}

object SampleGrpcAggregate extends ExtensionId[SampleGrpcAggregate] with ExtensionIdProvider {
  override def createExtension(system: ExtendedActorSystem): SampleGrpcAggregate = new SampleGrpcAggregate(system)
  override def lookup(): ExtensionId[_ <: Extension] = SampleGrpcAggregate
}
