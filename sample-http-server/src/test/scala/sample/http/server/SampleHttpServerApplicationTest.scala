package sample.http.server

import akka.actor.ActorSystem
import akka.testkit.TestKit
import fusion.http.FusionHttpServer
import fusion.test.FusionTestFunSuite
import org.scalatest.BeforeAndAfterAll

class SampleHttpServerApplicationTest extends TestKit(ActorSystem()) with FusionTestFunSuite with BeforeAndAfterAll {

  test("testSocketAddress") {
    val socketAddress = FusionHttpServer(system).component.socketAddress
    socketAddress.getPort mustBe 8000
  }

  test("testMain") {}

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    FusionHttpServer(system).component.startAbstractRouteSync(new Routes())
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
  }

}
