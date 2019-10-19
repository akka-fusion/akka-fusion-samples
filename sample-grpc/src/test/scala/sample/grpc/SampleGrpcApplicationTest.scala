package sample.grpc

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.grpc.GrpcClientSettings
import akka.stream.Materializer
import fusion.core.extension.FusionCore
import fusion.http.FusionHttpServer
import fusion.test.FusionTestWordSpec
import sample.HelloDTO
import sample.HelloService
import sample.HelloServiceClient

class SampleGrpcApplicationTest extends ScalaTestWithActorTestKit with FusionTestWordSpec {
  implicit private val ec = system.executionContext
  implicit private val mat: Materializer = Materializer(system)

  "SampleGrpcApplication" should {
    "sayHello" in {
      val dto = HelloDTO("Akka Fusion")
      val helloBO = HelloServiceClient(grpcClientSettings).sayHello(dto).futureValue
      helloBO.name shouldBe dto.name
      helloBO.result should not be empty
    }
  }

  private def grpcClientSettings = {
    GrpcClientSettings.fromConfig(HelloService.name)(FusionCore(system).classicSystem)
  }

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    FusionHttpServer(system).component.startAbstractRouteSync(new Routes())
  }
}
