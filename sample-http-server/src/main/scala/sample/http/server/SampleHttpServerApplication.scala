package sample.http.server

object SampleHttpServerApplication {

  def main(args: Array[String]): Unit = {
    val system = FusionUtils.createFromDiscovery()
    FusionHttpServer(system).component.startRouteSync(new Routes())
  }
}
