package sample.log

import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.StrictLogging

object SampleLogApplication extends StrictLogging {

  def main(args: Array[String]): Unit = {
    logger.info("SampleLogApplication startup.")
    logger.debug("sleep 5 seconds")
    TimeUnit.SECONDS.sleep(5)
    logger.info("SampleLogApplication shutdown.")
  }
}
