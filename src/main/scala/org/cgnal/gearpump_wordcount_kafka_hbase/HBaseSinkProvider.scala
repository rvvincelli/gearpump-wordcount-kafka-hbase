package org.cgnal.gearpump_wordcount_kafka_hbase

import akka.actor.ActorSystem
import org.apache.hadoop.hbase.HBaseConfiguration
import io.gearpump.cluster.UserConfig
import io.gearpump.external.hbase.HBaseSink
import io.gearpump.streaming.sink.DataSinkProcessor

trait HBaseSinkProvider { 
  
  implicit def actorSystem: ActorSystem
  
  private val hbaseSink = HBaseSink(UserConfig.empty, "loremipsum", HBaseConfiguration.create())
  
  protected val hbaseSinkProcessor = DataSinkProcessor(hbaseSink, 1)

}
