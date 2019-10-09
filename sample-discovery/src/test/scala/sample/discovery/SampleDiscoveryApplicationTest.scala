package sample.discovery

import akka.http.scaladsl.testkit.ScalatestRouteTest
import fusion.core.extension.FusionCore
import fusion.test.FusionTestWordSpec

/**
 * https://nacos.io/zh-cn/docs/quick-start.html 下载并安装 Nacos
 */
class SampleDiscoveryApplicationTest extends FusionTestWordSpec with ScalatestRouteTest {
  private val serverAddr = ""
  private val namespace = ""
  private val dataId = ""
  private val serviceName = ""

  "SampleDiscoveryApplicationTest" must {
    "serviceName" in {
      val configuration = FusionCore(system).configuration
      configuration.getBoolean("fusion.discovery.nacos.serviceName") mustBe serviceName
    }
  }

  override def testConfigSource: String = s"""fusion.discovery {
                                            |  enable = true
                                            |  nacos {
                                            |    serverAddr = $serverAddr
                                            |    namespace = $namespace
                                            |    dataId = $dataId
                                            |    group = "DEFAULT_GROUP"
                                            |    timeoutMs = 3000
                                            |    serviceName = $serviceName
                                            |    autoRegisterInstance = true
                                            |  }
                                            |}""".stripMargin

}
