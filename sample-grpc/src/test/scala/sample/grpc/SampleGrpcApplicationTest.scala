package sample.grpc

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.ActorMaterializer
import akka.stream.Materializer
import akka.testkit.TestKit
import fusion.http.FusionHttpServer
import fusion.test.FusionTestWordSpec
import org.scalatest.BeforeAndAfterAll
import sample.HelloDTO
import sample.HelloService
import sample.HelloServiceClient

class SampleGrpcApplicationTest
    extends TestKit(ActorSystem("SampleGrpcApplication"))
    with FusionTestWordSpec
    with BeforeAndAfterAll {
  import system.dispatcher
  implicit private val mat: Materializer = ActorMaterializer()

  "SampleGrpcApplication" must {
    "sayHello" in {
      val dto = HelloDTO("Akka Fusion")
      val helloBO = HelloServiceClient(grpcClientSettings).sayHello(dto).futureValue
      helloBO.name mustBe dto.name
      helloBO.result must not be empty
    }
  }

  private def grpcClientSettings = {
    GrpcClientSettings.fromConfig(HelloService.name)
  }

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    FusionHttpServer(system).component.startAbstractRouteSync(new Routes())
  }
}
