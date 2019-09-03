package sample.scheduler

import akka.actor.ActorSystem
import akka.testkit.TestKit
import fusion.jdbc.FusionJdbc
import fusion.jdbc.JdbcTemplate
import fusion.test.FusionTestFunSuite

class InitTablesTest extends TestKit(ActorSystem()) with FusionTestFunSuite {
  private val dataSource = FusionJdbc(system).component
  private val jdbcTemplate = JdbcTemplate(dataSource)

  test("create tables") {
    val sqlText = scala.io.Source.fromResource("sql/scheduler.sql").getLines().mkString("\n")
    jdbcTemplate.update(sqlText)
  }

}
