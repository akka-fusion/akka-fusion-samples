package sample.jdbc

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import fusion.jdbc.FusionJdbc
import fusion.jdbc.JdbcTemplate
import fusion.test.FusionTestWordSpec
import sample.jdbc.model.User

class SampleJdbcTest extends ScalaTestWithActorTestKit with FusionTestWordSpec {
  private val dataSource = FusionJdbc(system).component
  private val jdbcTemplate = JdbcTemplate(dataSource)

  "Sample Jdbc Test" should {
    "init" in {
      jdbcTemplate.update("""create table t_user(
          |  id bigserial primary key,
          |  name varchar(128) not null,
          |  age int,
          |  sex int,
          |  description text,
          |  created_at timestamp not null default now()
          |)""".stripMargin)
      jdbcTemplate.update("""insert into t_user(name, age, sex, description, created_at) values
          |('羊八井', 33, 1, '', now()),
          |('杨景', 33, 1, '', now())""".stripMargin) shouldBe 2
    }

    "count" in {
      jdbcTemplate.count("select count(*) from t_user") shouldBe 2
    }

    "list" in {
      val list = jdbcTemplate.listForMap("select * from t_user", Nil)
      list.size shouldBe 2
      val obj = list.head
      obj.get("age") shouldBe Some(33)
    }

    "query" in {
      val maybeUser = jdbcTemplate.namedFindForObject(
        """select id, name, age, sex, description, created_at from t_user
          |where name = ?name""".stripMargin,
        Map("name" -> "羊八井"),
        rs =>
          User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("age"),
            Option(rs.getInt("sex")),
            Option(rs.getString("description")),
            rs.getTimestamp("created_at").toLocalDateTime))
      maybeUser should not be empty
      val user = maybeUser.value
      user.age shouldBe 33
      user.sex shouldBe Some(1)
    }
  }
}
