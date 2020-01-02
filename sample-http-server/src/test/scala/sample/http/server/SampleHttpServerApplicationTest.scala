package sample.http.server

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import fusion.http.FusionHttpServer
import fusion.test.FusionTestFunSuite

class SampleHttpServerApplicationTest extends ScalaTestWithActorTestKit with FusionTestFunSuite {
  test("testSocketAddress") {
    val socketAddress = FusionHttpServer(system).component.socketAddress
    socketAddress.getPort shouldBe 8000
  }

  test("testMain") {}

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    FusionHttpServer(system).component.startAbstractRouteSync(new Routes())
  }
}
