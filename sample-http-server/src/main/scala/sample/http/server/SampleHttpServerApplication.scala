package sample.http.server

import fusion.core.util.FusionUtils
import fusion.http.FusionHttpServer

object SampleHttpServerApplication {

  def main(args: Array[String]): Unit = {
    val system = FusionUtils.createFromDiscovery()
    FusionHttpServer(system).component.startAbstractRouteSync(new Routes())
  }
}
