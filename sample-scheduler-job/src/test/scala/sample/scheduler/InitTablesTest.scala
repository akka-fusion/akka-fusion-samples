package sample.scheduler

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import fusion.jdbc.FusionJdbc
import fusion.jdbc.JdbcTemplate
import fusion.test.FusionTestFunSuite

class InitTablesTest extends ScalaTestWithActorTestKit with FusionTestFunSuite {
  private val dataSource = FusionJdbc(system).component
  private val jdbcTemplate = JdbcTemplate(dataSource)

  test("create tables") {
    val sqlText = scala.io.Source.fromResource("sql/scheduler.sql").getLines().mkString("\n")
    jdbcTemplate.update(sqlText)
  }

}
