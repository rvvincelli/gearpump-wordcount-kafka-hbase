package org.cgnal.gearpump_wordcount_kafka_hbase

import java.util.concurrent.TimeUnit

import akka.actor.Cancellable
import io.gearpump.streaming.task.{StartTime, Task, TaskContext}
import io.gearpump.Message
import io.gearpump.cluster.UserConfig

import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration

class Sum (taskContext : TaskContext, conf: UserConfig) extends Task(taskContext, conf) {
  import taskContext.output
  
  private[gearpump_wordcount_kafka_hbase] val map : mutable.HashMap[String, Long] = new mutable.HashMap[String, Long]()

  private[gearpump_wordcount_kafka_hbase] var wordCount : Long = 0
  private var snapShotTime : Long = System.currentTimeMillis()
  private var snapShotWordCount : Long = 0

  private var scheduler : Cancellable = null

  override def onStart(startTime : StartTime) : Unit = {
    scheduler = taskContext.schedule(new FiniteDuration(5, TimeUnit.SECONDS),
      new FiniteDuration(30, TimeUnit.SECONDS))(reportWordCount)
  }

  override def onNext(msg : Message) : Unit =
    if (null != msg) {
      val current = map.getOrElse(msg.msg.asInstanceOf[String], 0L)
      wordCount += 1
      val update = (msg.msg.asInstanceOf[String], "counts", "count", s"${current + 1}")
      map + ((update._1, update._4.toLong))
      output(new Message(update, System.currentTimeMillis()))
    }

  override def onStop() : Unit = {
    if (scheduler != null) {
      scheduler.cancel()
    }
  }

  def reportWordCount() : Unit = {
    val current : Long = System.currentTimeMillis()
    LOG.info(s"Task ${taskContext.taskId} Throughput: ${(wordCount - snapShotWordCount, (current - snapShotTime) / 1000)} (words, second)")
    snapShotWordCount = wordCount
    snapShotTime = current
  }
}
