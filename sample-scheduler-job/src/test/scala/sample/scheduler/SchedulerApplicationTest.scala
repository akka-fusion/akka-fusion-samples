package sample.scheduler

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.testkit.RouteTestTimeout
import akka.http.scaladsl.testkit.ScalatestRouteTest
import fusion.test.FusionTestSuite
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.Matchers
import sample.scheduler.model._
import sample.scheduler.route.SchedulerRoute

import scala.concurrent.duration._

class SchedulerApplicationTest
    extends FunSuite
    with ScalatestRouteTest
    with FusionTestSuite
    with BeforeAndAfterAll
    with Matchers {
  import fusion.json.json4s.http.Json4sSupport._
  private val route = new SchedulerRoute(system.toTyped).route
  implicit private val timeout = RouteTestTimeout(10.seconds)

  private var jobs: List[Key] = Nil
  private var triggerKeys: List[Key] = Nil

  override protected def createActorSystem(): ActorSystem = ActorSystem("sample-scheduler")

  test("Create Simple Job") {
    val payload = JobDTO(
      "test",
      data = Map("schedule" -> "Simple"),
      schedule = Some(TriggerSchedule(ScheduleType.SIMPLE, Some("5.seconds"))))
    Post("/scheduler/create", payload) ~> route ~> check {
      val jobBO = responseAs[JobBO]
      println(jobBO)
      status shouldBe StatusCodes.OK
      jobs ::= Key(jobBO.group, jobBO.name)
      triggerKeys ++= jobBO.triggers.map(bo => Key(bo.group, bo.name))
    }
  }

  test("Create Cron Job") {
    val payload = JobDTO(
      "test",
      data = Map("schedule" -> "Cron"),
      schedule = Some(TriggerSchedule(ScheduleType.CRON, cronExpression = Some("0/5 * * * * ?"))))
    Post("/scheduler/create", payload) ~> route ~> check {
      val jobBO = responseAs[JobBO]
      println(jobBO)
      status shouldBe StatusCodes.OK
      jobs ::= Key(jobBO.group, jobBO.name)
      triggerKeys ++= jobBO.triggers.map(bo => Key(bo.group, bo.name))
    }
  }

  test("Get Jobs") {
    jobs.foreach { jobKey =>
      val uri = Uri("/scheduler/item").withQuery(Uri.Query("group" -> jobKey.group, "name" -> jobKey.name))
      Get(uri) ~> route ~> check {
        val jobBO = responseAs[JobBO]
        println(jobBO)
        status shouldBe StatusCodes.OK
        jobBO.group shouldBe jobKey.group
        jobBO.name shouldBe jobKey.name
      }
    }
  }

  override protected def afterAll(): Unit = {
    TimeUnit.SECONDS.sleep(15)
    pause()
    super.afterAll()
  }

  private def pause(): Unit = {
    triggerKeys.foreach { triggerKey =>
      Post("/scheduler/cancel", JobCancelDTO(triggerKey = Some(triggerKey))) ~> route ~> check {
        status shouldBe StatusCodes.OK
      }
    }
  }
}
