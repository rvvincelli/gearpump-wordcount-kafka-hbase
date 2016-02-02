package org.cgnal.gearpump_wordcount_kafka_hbase

import java.io.File
import akka.actor.ActorSystem
import io.gearpump.cluster.UserConfig
import io.gearpump.external.hbase.HBaseSink
import io.gearpump.google.common.io.Files
import io.gearpump.streaming.sink.DataSinkProcessor
import org.apache.hadoop.conf.Configuration

trait HBaseSinkProvider { self: KafkaConfProvider =>
  
  implicit def actorSystem: ActorSystem
  
  private val principal = "rvvincelli@SERVER.ELIGOTECH.COM"
  private val file = Files.toByteArray(new File("/home/rvvincelli/rvvincelli.keytab"))
  
  private val userConfig = UserConfig.empty
    .withString("gearpump.kerberos.principal", principal)
    .withBytes("gearpump.keytab.file", file)
  
  private def hadoopConfig = {
    val conf = new Configuration()
    conf.set("hbase.zookeeper.quorum", zookeeperHost)
    conf.set("hbase.zookeeper.property.clientPort", zookeeperPort)
    conf
  }
    
  private lazy val hbaseSink = HBaseSink(userConfig, "randomipsum", hadoopConfig)
  
  protected lazy val hbaseSinkProcessor = DataSinkProcessor(hbaseSink, 1)

}
