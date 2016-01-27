package org.cgnal.gearpump_wordcount_kafka_hbase

import akka.actor.ActorSystem
import io.gearpump.streaming.kafka.{KafkaSource, KafkaStorageFactory}
import io.gearpump.streaming.source.DataSourceProcessor

trait KafkaSourceProvider {

  implicit def actorSystem: ActorSystem
  
  private val zookeepers = "fsbovcdhi01.fondiaria-sai.it:2181"
  private val brokers = "fsbovcdhi01.fondiaria-sai.it:9092"
  
  private val offsetStorageFactory = new KafkaStorageFactory(zookeepers, brokers)

  private val kafkaSource = new KafkaSource("loremipsum", zookeepers, offsetStorageFactory)
  
  protected val kafkaSourceProcessor = DataSourceProcessor(kafkaSource, 1)
  
}
