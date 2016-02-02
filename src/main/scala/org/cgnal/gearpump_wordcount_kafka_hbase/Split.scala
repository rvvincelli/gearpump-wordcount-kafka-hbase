package org.cgnal.gearpump_wordcount_kafka_hbase

import java.util.concurrent.TimeUnit
import io.gearpump.streaming.task.{StartTime, Task, TaskContext}
import io.gearpump.Message
import io.gearpump.cluster.UserConfig

class Split(taskContext : TaskContext, conf: UserConfig) extends Task(taskContext, conf) {
  import taskContext.{output, self}

  override def onNext(msg : Message) : Unit = {
    new String(msg.msg.asInstanceOf[Array[Byte]]).lines.foreach { line =>
      line.split("[\\s]+").filter(_.nonEmpty).foreach { msg =>
        output(new Message(msg, System.currentTimeMillis()))
      }
    }

    import scala.concurrent.duration._
    taskContext.scheduleOnce(Duration(100, TimeUnit.MILLISECONDS))(self ! Message("continue", System.currentTimeMillis()))
  }
}
