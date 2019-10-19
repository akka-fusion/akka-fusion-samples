package sample.http.gateway

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import fusion.http.FusionHttpServer
import fusion.http.client.DefaultHttpClient
import fusion.test.FusionTestFunSuite

class SampleHttpGatewayApplicationTest extends ScalaTestWithActorTestKit with FusionTestFunSuite {
  implicit private val mat = Materializer(system)

  test("proxy success") {
    val uri = FusionHttpServer(system).component.buildUri("/api/hello")
    val response = DefaultHttpClient(system.toClassic).singleRequest(HttpMethods.POST, uri).futureValue
    response.status shouldBe StatusCodes.OK
  }

  test("proxy failure") {
    val uri = FusionHttpServer(system).component.buildUri("/not-exists/path")
    val response = DefaultHttpClient(system.toClassic).singleRequest(HttpMethods.POST, uri).futureValue
    response.status shouldBe StatusCodes.ServiceUnavailable
  }

  override protected def beforeAll(): Unit = {
    import akka.http.scaladsl.server.Directives._
    super.beforeAll()
    val route = pathPrefix("api") {
      extractRequest { request =>
        complete(HttpResponse(entity = request.entity))
      }
    }
    FusionHttpServer(system).component.startAbstractRouteSync(new Routes(system))
    FusionHttpServer(system).components.lookup("sample.http1").startRouteSync(route)
    FusionHttpServer(system).components.lookup("sample.http2").startRouteSync(route)
  }

}
