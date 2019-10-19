package sample.grpc

import akka.actor.typed.ActorSystem
import akka.stream.Materializer
import fusion.core.extension.FusionExtension
import fusion.core.extension.FusionExtensionId
import sample.HelloServicePowerApi
import sample.HelloServicePowerApiHandler
import sample.grpc.service.HelloServiceImpl

class SampleGrpcAggregate(override val system: ActorSystem[_]) extends FusionExtension {
  implicit val mat: Materializer = Materializer(system)
  implicit val st = classicSystem

  val helloService: HelloServicePowerApi = new HelloServiceImpl()(system, mat)

  val grpcHandlers = List(HelloServicePowerApiHandler.partial(helloService))
}

object SampleGrpcAggregate extends FusionExtensionId[SampleGrpcAggregate] {
  override def createExtension(system: ActorSystem[_]): SampleGrpcAggregate = new SampleGrpcAggregate(system)
}
