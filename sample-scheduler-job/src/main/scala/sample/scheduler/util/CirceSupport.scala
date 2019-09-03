package sample.scheduler.util

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe._
import io.circe.syntax._
import io.circe.generic.AutoDerivation
import org.quartz.JobKey
import org.quartz.TriggerKey
import sample.scheduler.model.ScheduleType

object CirceSupport extends FailFastCirceSupport with AutoDerivation with JobAutoDerivation {}

trait JobAutoDerivation {
  implicit val scheduleTypeEncoder: Encoder[ScheduleType] = (a: ScheduleType) => Json.fromString(a.name)
  implicit val schedulerTypeDecoder: Decoder[ScheduleType] = (c: HCursor) => {
    val v1 = c.as[Int].map(ScheduleType.fromValue)
    if (v1.isRight) {
      v1
    } else {
      c.as[String].map(ScheduleType.fromName).flatMap {
        case Some(ScheduleType.Unrecognized(_)) => Left(DecodingFailure("无效的ScheduleType", Nil))
        case Some(value)                        => Right(value)
        case _                                  => Left(DecodingFailure("无效的ScheduleType", Nil))
      }
    }
  }
  implicit val jobKeyEncoder: Encoder[JobKey] = (a: JobKey) =>
    Json.obj("group" -> a.getGroup.asJson, "name" -> a.getName.asJson)
  implicit val triggerKeyEncoder: Encoder[TriggerKey] = (a: TriggerKey) =>
    Json.obj("group" -> a.getGroup.asJson, "name" -> a.getName.asJson)
}
