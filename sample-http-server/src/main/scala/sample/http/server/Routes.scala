package sample.http.server

class Routes() extends AbstractRoute {

  override def route: Route = pathPrefix("sample") {
    path("hello") {
      complete("hello world!")
    }
  }
}
