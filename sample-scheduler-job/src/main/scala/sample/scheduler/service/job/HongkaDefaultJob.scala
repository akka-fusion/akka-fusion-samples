package sample.scheduler.service.job

import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpRequest
import com.typesafe.scalalogging.StrictLogging
import fusion.core.util.FusionUtils
import fusion.job.ScheduleJob
import fusion.json.JsonUtils
import helloscala.common.util.StringUtils
import org.json4s.Extraction
import org.json4s.JsonAST.JObject
import org.quartz.JobExecutionContext
import sample.scheduler.constant.JobConstants

import scala.jdk.CollectionConverters._
import scala.util.Failure
import scala.util.Success

class HongkaDefaultJob extends ScheduleJob with StrictLogging {
  override def execute(context: JobExecutionContext): Unit = {
    performCallback(context)
  }

  private def performCallback(context: JobExecutionContext): Unit = {
    import JsonUtils.defaultFormats

    val dataMap = context.getMergedJobDataMap.asScala.mapValues(_.toString)
    val callback = dataMap.getOrElse(JobConstants.CALLBACK, "")

    if (StringUtils.isNoneBlank(callback) && callback.startsWith("http")) {
      implicit val system = FusionUtils.actorSystem().toClassic
      import system.dispatcher

      val data = JObject(
        "data" -> Extraction.decompose(dataMap),
        "jobKey" -> Extraction.decompose(context.getJobDetail.getKey),
        "triggerKey" -> Extraction.decompose(context.getTrigger.getKey))

      val request =
        HttpRequest(
          HttpMethods.POST,
          callback,
          entity = HttpEntity(ContentTypes.`application/json`, JsonUtils.compact(data)))

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
