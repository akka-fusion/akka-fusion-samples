package sample.http.gateway

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.RouteTestTimeout
import akka.http.scaladsl.testkit.ScalatestRouteTest
import fusion.http.gateway.server.FusionHttpGateway
import fusion.test.FusionTestFunSuite

import scala.concurrent.duration._

class RoutesTest extends FusionTestFunSuite with ScalatestRouteTest {
  private val gatewayRoute = FusionHttpGateway(system).component

  test("routes") {
    implicit val timeout = RouteTestTimeout(30.seconds)
    Get("/api/v4/statistics/dashboard") ~> gatewayRoute ~> check {
      println(response)
      status mustBe StatusCodes.ServiceUnavailable
    }
  }

}
