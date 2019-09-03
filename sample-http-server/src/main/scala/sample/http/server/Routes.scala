package sample.http.server

import akka.http.scaladsl.server.Route
import fusion.http.server.AbstractRoute

class Routes() extends AbstractRoute {

  override def route: Route = pathPrefix("sample") {
    path("hello") {
      complete("hello world!")
    }
  }
}
