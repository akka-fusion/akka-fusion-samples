package sample.http.client

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import fusion.http.FusionHttpServer
import fusion.http.client.DefaultHttpClient
import fusion.json.jackson.Jackson
import fusion.test.FusionTestWordSpec
import org.json4s.JsonAST.JObject
import org.json4s.JsonAST.JString
import org.json4s.JsonAST.JValue
import org.scalatest.BeforeAndAfterAll

class SampleHttpClientTest extends TestKit(ActorSystem()) with FusionTestWordSpec with BeforeAndAfterAll {
  implicit private val mat: ActorMaterializer = ActorMaterializer()
  import system.dispatcher

  "HttpClient echo" must {
    "Ok" in {
      import fusion.json.json4s.http.Json4sSupport._
      val socketAddress = FusionHttpServer(system).component.socketAddress
      val uri = s"http://${socketAddress.getAddress.getHostAddress}:${socketAddress.getPort}/api/echo"
      val entity = JObject("hello" -> JString("world"))
      val result = DefaultHttpClient(system)
        .singleRequest(HttpMethods.POST, uri, entity = entity)
        .flatMap(resp => Unmarshal(resp.entity).to[JValue])
        .futureValue
      result mustBe entity
    }

    "NotFound" in {
      import fusion.json.jackson.http.JacksonSupport._
      val socketAddress = FusionHttpServer(system).component.socketAddress
      val uri = s"http://${socketAddress.getAddress.getHostAddress}:${socketAddress.getPort}/api/not-found"
      val entity = Jackson.createObjectNode
      entity.put("hello", "world")
      val response = DefaultHttpClient(system).singleRequest(HttpMethods.POST, uri, entity = entity).futureValue
      response.status mustBe StatusCodes.NotFound
    }
  }

  override protected def beforeAll(): Unit = {
    import akka.http.scaladsl.server.Directives._
    import fusion.json.json4s.http.Json4sSupport._
    super.beforeAll()
    val route = pathPrefix("api") {
      (path("echo") & post) {
        entity(as[JValue]) { payload =>
          complete(payload)
        }
      }
    }
    FusionHttpServer(system).component.startRouteSync(route)
  }

}
