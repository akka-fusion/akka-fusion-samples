package sample.slick.schema

import akka.actor.typed.ActorSystem
import fusion.jdbc.FusionJdbc
import fusion.slick.FusionJdbcProfile
import slick.jdbc.PostgresProfile

trait CustomProfile extends PostgresProfile with FusionJdbcProfile {
  override val api = MyAPI
  object MyAPI extends API with FusionImplicits {}
}

object CustomProfile extends CustomProfile

import sample.slick.schema.CustomProfile.api._

class SlickSchema(system: ActorSystem[_]) extends PersonTable {
  val db = databaseForDataSource(FusionJdbc(system).component)

  val ddl = personTable.schema
}
