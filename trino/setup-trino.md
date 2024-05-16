# 
## _Manually Install Trino Cluster_

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://trino.io/)
- ✨Trino + Apache Superset✨
- Trino là một query enginee có thể tích hợp với nhiều data source như: HDFS, Elasticsearch, Kafka, HBase,...
- Superset giúp truy vấn và visualize dữ liệu trên Web Browsew (ở đây sẽ tích hợp với Trino như một data back end )

## Resource: 
| Resource | Link Download                                                                                                      |
| --------|--------------------------------------------------------------------------------------------------------------------|
| HMS | [Link HMS](http://apache.uvigo.es/hive/hive-standalone-metastore-3.0.0/hive-standalone-metastore-3.0.0-bin.tar.gz) |
| Superset | [Link Superset](https://superset.apache.org/docs/installation/installing-superset-from-scratch)                    |
| Trino | [Link Trino](https://trino.io/docs/current/installation/deployment.html)                                           |

## Preinstall
- Linux distro (Ubuntu)
- Python 3 
- Java 11

## Install

### 1. Hive MetaStore Server(HMS) 
`HMS có thể install độc lập, miễn sao Trino cluster có thể call tới Server Thrift của HMS`
`Chỉ cần cài độc lập HMS mà không cần cài hive runtime`
> HMS có nhiệm vụ lưu trữ schema trên cụm HDFS tương ứng do người dùng định nghĩa. 
> HMS giúp Trino xác định được vị trí của dữ liệu trong cụm HDFS 
> More: https://trino.io/blog/2020/10/20/intro-to-hive-connector.html
> https://cwiki.apache.org/confluence/display/Hive/AdminManual+Metastore+3.0+Administration
> 
> HMS gồm: 
- Thrift Server để giao tiếp với Trino
- Database để lưu trữ Schema 

Overview: 
- Config để HMS có thể đọc được thông tin trong HDFS: file config cụm hdfs, giao tiếp
- Database để lưu metadata cho cụm HDFS
- Luồng khởi tạo: INIT SERVER -> (INIT SCHEMA nếu chưa khởi tạo) -> START SERVER

- Bước 1: 
- Download [Link HMS](http://apache.uvigo.es/hive/hive-standalone-metastore-3.0.0/hive-standalone-metastore-3.0.0-bin.tar.gz)
- Download [Hadoop Bin](https://dlcdn.apache.org/hadoop/common/hadoop-3.3.1/hadoop-3.3.1.tar.gz)
- Download DB driver [MYSQL](https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.22/mysql-connector-java-8.0.22.jar) >> Move to .(hms)/lib/

Bước 2:
- Config conf/metastore-site.xml: (Chú ý tạo user-password cho HMS trong RDBMS)
```sh
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
  <property>
    <name>metastore.thrift.uris</name>
    <value>${thrift://<localhost>:<port>}</value>
  </property>
  <property>
    <name>metastore.thrift.port</name>
    <value>${port}</value>
  </property>
  <!-- Change if use other DB -->
  <property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>${jdbc:mysql://<host>:<port>/<schema>?createDatabaseIfNotExist=true&amp;useSSL=false}</value>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionDriverName</name>
    <value>com.mysql.jdbc.Driver</value>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionUserName</name>
    <value>${username-db}</value>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionPassword</name>
    <value>${password-db}</value>
  </property>
  <property>
    <name>metastore.task.threads.always</name>
    <value>org.apache.hadoop.hive.metastore.events.EventCleanerTask</value>
  </property>
  <property>
    <name>metastore.expression.proxy</name>
    <value>org.apache.hadoop.hive.metastore.DefaultPartitionExpressionProxy</value>
  </property>
  <property>
    <name>hive.metastore.schema.verification</name>
    <value>false</value>
  </property>
  <property>
    <name>metastore.storage.schema.reader.impl</name>
    <value>org.apache.hadoop.hive.metastore.SerDeStorageSchemaReader</value>
  </property>
<!--If HDFS use Kerberos Authen -->
<property>
  <name>hive.server2.authentication</name>
  <value>${KERBEROS}</value>
</property>
<property>
  <name>metastore.kerberos.principal</name>
  <value>${principal-hive}</value>
</property>
<property>
  <name>metastore.kerberos.keytab.file</name>
  <value>${path-to-hive-keytab}</value>
</property>
<property>
  <name>hive.metastore.sasl.enabled</name>
  <value>true</value>
</property>
</configuration>
```
- Config bin/metastore-config.sh
```sh
Add Hadoop path: export HADOOP_HOME = ${hadoop-path}
```
Bước 3:
- Run cmd để khởi tạo schema cho HMS trong DB (Chỉ cần khởi tạo lần đầu)
```sh
    bin/schematool –initschema –dbtype mysql
```
- __(Chú ý khởi tạo keytab trước khi chạy)__
- Run HMS:
```sh
    bin/start-metastore
```

``` 
Following this [docs](https://trino.io/docs/current/installation/deployment.html)

### Apache Superset
Following this [docs](https://superset.apache.org/docs/installation/installing-superset-from-scratch)


### Note 1 số lỗi khi cài đặt

* Sử dụng các lib log4j sau thay vì các default log4j khi cài đặt HMS: <b>log4j-1.2.16.jar  log4j-api-2.15.0.jar  log4j-core-2.15.0.jar</b> để hiện rõ các lỗi khi chạy.
* Sử dụng config `hive.metastore.sasl.enabled` = true trong `metastore-site.xml` để sửa khi dùng hdfs authen kerberos: 
```
ERROR server.TThreadPoolServer: Thrift error occurred during processing of message.                                                                                                                
org.apache.thrift.protocol.TProtocolException: Missing version in readMessageBegin, old client?                                                                                                                      
        at org.apache.thrift.protocol.TBinaryProtocol.readMessageBegin(TBinaryProtocol.java:228)                                                                                                                     
        at org.apache.hadoop.hive.metastore.TUGIBasedProcessor.process(TUGIBasedProcessor.java:76)                                                                                                                   
        at org.apache.thrift.server.TThreadPoolServer$WorkerProcess.run(TThreadPoolServer.java:286)                                                                                                                  
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)                                                                                                                           
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)                                                                                                                           
        at java.lang.Thread.run(Thread.java:745)                        
```

* Đường dẫn `HADOOP_HOME` khi export để chạy HMS cần sửa lại <b>hdfs-site.xml, core-site.xml</b> giống như trên cụm hadoop cần kết nối. Và cần cùng phiên bản với cụm hadoop.