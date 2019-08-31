package sample.http.gateway

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import fusion.http.FusionHttpServer
import fusion.http.client.DefaultHttpClient
import fusion.test.FusionTestFunSuite
import org.scalatest.BeforeAndAfterAll

class SampleHttpGatewayApplicationTest extends TestKit(ActorSystem()) with FusionTestFunSuite with BeforeAndAfterAll {
  implicit private val mat: ActorMaterializer = ActorMaterializer()

  test("proxy success") {
    val uri = FusionHttpServer(system).component.buildUri("/api/hello")
    val response = DefaultHttpClient(system).singleRequest(HttpMethods.POST, uri).futureValue
    response.status mustBe StatusCodes.OK
  }

  test("proxy failure") {
    val uri = FusionHttpServer(system).component.buildUri("/not-exists/path")
    val response = DefaultHttpClient(system).singleRequest(HttpMethods.POST, uri).futureValue
    response.status mustBe StatusCodes.ServiceUnavailable
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
