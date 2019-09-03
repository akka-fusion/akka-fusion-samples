package sample.slick.model

import java.sql.Timestamp
import java.time.Instant

case class Person(
    id: Long,
    name: String,
    age: Option[Int] = None,
    sex: Option[Int] = None,
    description: Option[String] = None,
    createdAt: Timestamp = Timestamp.from(Instant.now()))
