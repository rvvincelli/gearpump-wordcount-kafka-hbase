package org.cgnal.gearpump_wordcount_kafka_hbase

import akka.actor.ActorSystem
import io.gearpump.streaming.{StreamApplication, Processor}
import io.gearpump.cluster.UserConfig
import io.gearpump.cluster.client.ClientContext
import io.gearpump.cluster.main.{ArgumentsParser, CLIOption, ParseResult}
import io.gearpump.partitioner.HashPartitioner
import io.gearpump.util.Graph.Node
import io.gearpump.util.{AkkaApp, Graph, LogUtil}
import org.slf4j.Logger

object GearpumpWordcountKafkaHBaseApp extends AkkaApp with ArgumentsParser with KafkaSourceProvider with HBaseSinkProvider {
  
  implicit def actorSystem = ActorSystem()

  override val options: Array[(String, CLIOption[Any])] = Array(
    "split" -> CLIOption[Int]("<how many split tasks>", required = false, defaultValue = Some(1)),
    "sum" -> CLIOption[Int]("<how many sum tasks>", required = false, defaultValue = Some(1))
  )

  def application(config: ParseResult) : StreamApplication = {
    val splitNum = config.getInt("split")
    val sumNum = config.getInt("sum")
    val split = Processor[Split](splitNum)
    val sum = Processor[Sum](sumNum)
    val partitioner = new HashPartitioner

    val app = StreamApplication("wordCount", Graph(kafkaSourceProcessor ~> split ~ partitioner ~> sum ~> hbaseSinkProcessor), UserConfig.empty)
    app
  }

  override def main(akkaConf: Config, args: Array[String]): Unit = {
    val config = parse(args)

    val context = ClientContext(akkaConf)

    val app = application(config)
    context.submit(app)
    context.close()

  }
}
