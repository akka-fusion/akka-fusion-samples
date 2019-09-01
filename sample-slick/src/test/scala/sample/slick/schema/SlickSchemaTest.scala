package sample.slick.schema

import java.sql.Timestamp
import java.time.Instant

import akka.actor.ActorSystem
import akka.testkit.TestKit
import fusion.test.FusionTestWordSpec
import helloscala.common.exception.HSNotFoundException
import sample.slick.model.Person

class SlickSchemaTest extends TestKit(ActorSystem()) with FusionTestWordSpec {
  private val schema = new SlickSchema(system)
  import sample.slick.schema.CustomProfile.api._
  import schema._

  "ddl" must {
    "print" in {
      val ddls = schema.ddl.createStatements.toList
      ddls must not be empty
      ddls.foreach(println)
    }
    "execute" in {
      db.run(schema.ddl.create.transactionally).futureValue
    }
  }

  "dml" must {
    val now = Timestamp.from(Instant.now())

    "insert" in {
      val action = personTable += Person(0, "羊八井", createdAt = now)
      db.run(action.transactionally).futureValue mustBe 1
    }

    "query" in {
      val query = personTable.filter(t => t.name === "羊八井").result.headOption
      val maybe = db.run(query).futureValue
      maybe must not be empty
      val person = maybe.value
      person.id mustBe 1
      person.name mustBe "羊八井"
      person.createdAt mustBe now
    }

    "update" in {
      import system.dispatcher
      val id = 1L
      val query = personTable.filter(_.id === id)
      val action = query.result.headOption.flatMap {
        case Some(u) =>
          val payload = u.copy(name = "杨景", age = Some(33))
          query.update(payload)
        case _ => DBIO.failed(HSNotFoundException(s"用户未找到，ID: $id"))
      }
      val ret = db.run(action.transactionally).futureValue
      ret mustBe 1
    }

    "delete" in {
      val ret = db.run(personTable.filter(_.id === 1L).delete.transactionally).futureValue
      ret mustBe 1
    }

    "count" in {
      db.run(personTable.size.result).futureValue mustBe 0
    }
  }

}
