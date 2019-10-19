package sample.slick.schema

import java.sql.Timestamp
import java.time.Instant

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import fusion.test.FusionTestWordSpec
import helloscala.common.exception.HSNotFoundException
import sample.slick.model.Person

class SlickSchemaTest extends ScalaTestWithActorTestKit with FusionTestWordSpec {
  private val schema = new SlickSchema(system)
  import sample.slick.schema.CustomProfile.api._
  import schema._

  "ddl" should {
    "print" in {
      val ddls = schema.ddl.createStatements.toList
      ddls should not be empty
      ddls.foreach(println)
    }
    "execute" in {
      db.run(schema.ddl.create.transactionally).futureValue
    }
  }

  "dml" should {
    val now = Timestamp.from(Instant.now())

    "insert" in {
      val action = personTable += Person(0, "羊八井", createdAt = now)
      db.run(action.transactionally).futureValue shouldBe 1
    }

    "query" in {
      val query = personTable.filter(t => t.name === "羊八井").result.headOption
      val maybe = db.run(query).futureValue
      maybe should not be empty
      val person = maybe.value
      person.id shouldBe 1
      person.name shouldBe "羊八井"
      person.createdAt shouldBe now
    }

    "update" in {
      implicit val ec = system.executionContext
      val id = 1L
      val query = personTable.filter(_.id === id)
      val action = query.result.headOption.flatMap {
        case Some(u) =>
          val payload = u.copy(name = "杨景", age = Some(33))
          query.update(payload)
        case _ => DBIO.failed(HSNotFoundException(s"用户未找到，ID: $id"))
      }
      val ret = db.run(action.transactionally).futureValue
      ret shouldBe 1
    }

    "delete" in {
      val ret = db.run(personTable.filter(_.id === 1L).delete.transactionally).futureValue
      ret shouldBe 1
    }

    "count" in {
      db.run(personTable.size.result).futureValue shouldBe 0
    }
  }

}
