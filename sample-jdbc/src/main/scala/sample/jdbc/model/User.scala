package sample.jdbc.model

import java.time.LocalDateTime

case class User(
    id: Long,
    name: String,
    age: Int,
    sex: Option[Int],
    description: Option[String],
    createdAt: LocalDateTime)
