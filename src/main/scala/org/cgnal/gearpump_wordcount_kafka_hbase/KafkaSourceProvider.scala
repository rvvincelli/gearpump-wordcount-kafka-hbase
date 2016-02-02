package org.cgnal.gearpump_wordcount_kafka_hbase

import akka.actor.ActorSystem
import io.gearpump.streaming.kafka.{KafkaSource, KafkaStorageFactory}
import io.gearpump.streaming.source.DataSourceProcessor

trait KafkaSourceProvider { self: KafkaConfProvider =>

  implicit def actorSystem: ActorSystem
  
  private lazy val zookeepers = s"$zookeeperHost:$zookeeperPort"
  private lazy val brokers = s"$brokerHost:$brokerPort"
  
  private lazy val offsetStorageFactory = new KafkaStorageFactory(zookeepers, brokers)

  private lazy val kafkaSource = new KafkaSource("randomipsum", zookeepers, offsetStorageFactory)
  
  protected lazy val kafkaSourceProcessor = DataSourceProcessor(kafkaSource, 1)
  
}
