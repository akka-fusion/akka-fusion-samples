package fusion.http.gateway.server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri
import akka.testkit.TestKit
import fusion.test.FusionTestFunSuite
import io.circe.Encoder
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.duration.FiniteDuration

class GatewaySettingTest extends TestKit(ActorSystem()) with FusionTestFunSuite {
  implicit val durationEncoder = new Encoder[FiniteDuration] {
    override def apply(a: FiniteDuration): Json = Json.fromString(a.toString())
  }
  test("setting") {
    val gatewaySetting = GatewaySetting.fromActorSystem(system, "fusion.http.default.gateway")
    println(gatewaySetting)
//    println(Jackson.prettyStringify(gatewaySetting))
  }

  test("uri") {
    val uri1 = Uri("http://10.0.0.7:8888/api/v4/account/user/page")
    val uri2 = Uri("/api/v4/account/user/page")
    println(uri1)
    println(uri2)
    println(uri1.asJson.noSpaces)
    println(uri2.asJson.noSpaces)
  }

}
