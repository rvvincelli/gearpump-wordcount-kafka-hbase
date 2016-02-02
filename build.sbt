import sbtassembly.Plugin.AssemblyKeys._

assemblySettings

name := "gearpump-wordcount-kafka-hbase"

version := "1.0"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-deprecation")

resolvers ++= Seq(
  "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/",
  "Patrik at Bintray" at "http://dl.bintray.com/patriknw/maven",
  "Cloudera repo" at "https://repository.cloudera.com/artifactory/cloudera-repos",
  "vincent" at "http://dl.bintray.com/fvunicorn/maven",
  "non" at "http://dl.bintray.com/non/maven",
  "Maven repo" at "http://repo.maven.apache.org/maven2",
  "Maven1 repo" at "http://repo1.maven.org/maven2",
  "Maven2 repo" at "http://mvnrepository.com/artifact"
)

val gearpumpVersion = "0.7.5"
val hadoopVersion = "2.6.0-cdh5.4.2"

libraryDependencies ++= Seq(
  "com.github.intel-hadoop"                %  "gearpump-core_2.11"         % gearpumpVersion	withSources(),
  "com.github.intel-hadoop"                %  "gearpump-streaming_2.11"    % gearpumpVersion	withSources(),
  "com.github.intel-hadoop"                %% "gearpump-external-kafka"    % gearpumpVersion	withSources(),
  "com.github.intel-hadoop"                %% "gearpump-external-hbase"    % gearpumpVersion	withSources(),
  "org.apache.hadoop"			   %  "hadoop-common"		   % hadoopVersion	% "provided" //this shouldn't be needed but it won't compile without - library bug ?
)

mergeStrategy in assembly := {
  case s if s endsWith "geardefault.conf"                   => MergeStrategy.first
  case s if s endsWith "log4j.properties"                   => MergeStrategy.discard
  case s if s endsWith "DEPENDENCIES"                       => MergeStrategy.discard
  case x 					     	    =>
    val oldStrategy = (mergeStrategy in assembly).value
    oldStrategy(x)
}
