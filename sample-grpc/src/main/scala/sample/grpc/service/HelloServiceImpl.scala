package sample.grpc.service

import java.time.OffsetDateTime

import akka.NotUsed
import akka.actor.ActorSystem
import akka.grpc.scaladsl.Metadata
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import sample.HelloBO
import sample.HelloDTO
import sample.HelloServicePowerApi

import scala.concurrent.Future

class HelloServiceImpl()(implicit system: ActorSystem, mat: Materializer) extends HelloServicePowerApi {
  import system.dispatcher

  override def sayHello(in: HelloDTO, metadata: Metadata): Future[HelloBO] = Future {
    HelloBO(in.name, "result")
  }

  override def keepsHello(in: Source[HelloDTO, NotUsed], metadata: Metadata): Future[HelloBO] = {
    in.runWith(Sink.seq).map(hellos => HelloBO(s"Hello, ${hellos.map(_.name).mkString(", ")}", "result"))
  }

  override def keepsResult(in: HelloDTO, metadata: Metadata): Source[HelloBO, NotUsed] = {
    Source(s"Hello, ${in.name}".toList).map(c => HelloBO(in.name, c.toString))
  }

  override def streamHellos(in: Source[HelloDTO, NotUsed], metadata: Metadata): Source[HelloBO, NotUsed] = {
    in.map(hello => HelloBO(hello.name, OffsetDateTime.now().toString))
  }
}
