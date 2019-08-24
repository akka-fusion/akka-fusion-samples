package fusion.http.gateway.server

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.PathMatchers
import akka.http.scaladsl.testkit.ScalatestRouteTest
import fusion.test.FusionTestFunSuite

class RoutePathTest extends FusionTestFunSuite with ScalatestRouteTest {
  test("path") {
    import akka.http.scaladsl.server.Directives._
    val route = pathPrefix(PathMatchers.separateOnSlashes("api/v4/account")) {
      extractRequest { request =>
        complete(HttpResponse(entity = HttpEntity(request.uri.toString())))
      }
    }

    Get("/api/v4/account/area/listAll") ~> route ~> check {
      val text = responseAs[String]
      println(text)
      status mustBe StatusCodes.OK
    }
  }

}
