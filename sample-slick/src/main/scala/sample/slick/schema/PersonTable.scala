package sample.slick.schema

import java.sql.Timestamp

import sample.slick.model.Person
import sample.slick.schema.CustomProfile.api._

trait PersonTable {
  class PersonTable(tag: Tag) extends Table[Person](tag, "t_person") {
    val id = column[Long]("id", O.AutoInc, O.PrimaryKey)
    val name = column[String]("name")
    val age = column[Option[Int]]("age")
    val sex = column[Option[Int]]("sex")
    val description = column[Option[String]]("description", O.SqlType("text"))
    val createdAt = column[Timestamp]("created_at")
    override def * = (id, name, age, sex, description, createdAt).mapTo[Person]
  }

  val personTable = TableQuery[PersonTable]
}
