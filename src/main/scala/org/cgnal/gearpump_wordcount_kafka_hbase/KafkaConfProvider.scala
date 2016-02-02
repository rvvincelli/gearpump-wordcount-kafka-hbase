package org.cgnal.gearpump_wordcount_kafka_hbase

trait KafkaConfProvider {

  protected val brokerHost = "eligo109"
  protected val brokerPort = "9092"

  protected val zookeeperHost = "eligo105"
  protected val zookeeperPort = "2181"
  
}
