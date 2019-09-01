package sample.scheduler.service.job

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import com.typesafe.scalalogging.StrictLogging
import fusion.core.extension.FusionCore
import fusion.core.util.FusionUtils
import fusion.http.util.HttpUtils
import fusion.job.ScheduleJob
import helloscala.common.util.StringUtils
import io.circe.Json
import org.quartz.JobExecutionContext
import sample.scheduler.constant.JobConstants

import scala.collection.JavaConverters._
import scala.util.Failure
import scala.util.Success

class HongkaDefaultJob extends ScheduleJob with StrictLogging {
  override def execute(context: JobExecutionContext): Unit = {
    performCallback(context)
  }

  private def performCallback(context: JobExecutionContext): Unit = {
    import io.circe.syntax._
    import io.circe.generic.auto._

    val dataMap = context.getMergedJobDataMap.asScala.mapValues(_.toString)
    val callback = dataMap.getOrElse(JobConstants.CALLBACK, "")

    if (StringUtils.isNoneBlank(callback) && callback.startsWith("http")) {
      implicit val system = FusionUtils.actorSystem()
      import system.dispatcher

      val data = Json.obj(
        "data" -> dataMap.asJson,
        "jobKey" -> context.getJobDetail.getKey.asJson,
        "triggerKey" -> context.getTrigger.getKey.asJson)

      val request =
        HttpRequest(HttpMethods.POST, callback, entity = HttpEntity(ContentTypes.`application/json`, data.noSpaces))

      val responseF = Http().singleRequest(request)

      responseF.onComplete {
        case Success(response) =>
          logger.debug(s"向远程服务发送回调错误完成，[${detailTrigger(context)}] callback地址：$callback。响应：$response")
        case Failure(e) =>
          logger.error(s"向远程服务发送回调错误，[${detailTrigger(context)}] callback地址：$callback", e)
      }
    }

  }

}
