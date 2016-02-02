##A toy Gearpump application

This is a small [Gearpump](http://www.gearpump.io) application whose purpose is to load a text document from [Kafka](http://kafka.apache.org), compute its wordcount, store the results in a [HBase](http://hbase.apache.org) table. The text is stored in Kafka as a topic, where each message is a single line. The result is a HBase table with words as rowkeys and a count column.

###Prequisites
The application reads from a Kafka topic; topics can be created on-the-fly, for example to pour a textfile onto Kafka - a message per line - simply do:

`kafka-console-producer --broker-list eligo109:9092 --topic loremipsum --new-producer < randomipsum.txt`

HBase tables instead need to be created explicitly, and it looks the like Gearpump connector does not do that on your behalf. For the hardcoded values the corresponding table was  created on the cluster but in general use `hbase shell` to input the DDL commands to create a table with a given name, column families and fields:

`$hbase shell`
`>create 'table','column_family'`

this creates a new table in the `default` namespace.

###Deployment

We assume to work on a Cloudera CDH installation and the code is already configured to take into account Kerberos security - it is hardcoded though, make sure to review and change it first. Here are the steps:

 1. Download the Gearpump precompiled package [here](http://www.gearpump.io/download.html)
 2. Upload it to a suitable HDFS location, eg. `/usr/lib` or your home
 3. Unzip the package and `cd` there
 4. Copy the contents of your Hadoop configuration directory, `/etc/hadoop` into the `conf` subdir
 5. Launch the Gearpump cluster Yarn application, eg: `bin/yarnclient launch -package /user/rvvincelli/gearpump-2.11-0.7.5.zip`; your application should appear under the Applications tab for Yarn
 6. Get a copy of the active configuration: `bin/yarnclient getconfig -appid <APPID> -output conf/mycluster.conf` where `<APPID>` is the application ID from the tab above; you can't use the configuration from previous Gearpump cluster runs, fetch it anew
 7. Launch the application: `bin/gear app -jar ../gearpump-wordcount-kafka-hbase-assembly-1.0.jar -conf conf/mycluster.conf`
 8. If you see `Submit application succeed` in the output then everything went fine!

###Debug

First of all make sure the application is running by connecting to the Gearpump webapp (see [here](http://www.gearpump.io/releases/latest/deployment-ui-authentication.html)) - you find the link in the container logs of the Gearpump instance on the Yarn Resource Manager webapp. Once you log in click on the `Details` button for your application - if it is not enabled then the application has already terminated time ago. In the `Overview` tab click on `Log dir.` and move to this path on the box where the appmaster actor is running - you see this in the `Actor Path` entry.

To make sure the data is there fire up an HBase shell and scan the table:

`t = get_table 'randomipsum'`
`t.scan`
