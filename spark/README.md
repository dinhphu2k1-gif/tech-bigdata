SPARK
===

Sau khi cài xong Hadoop sẽ cài tiếp Spark.
Có thể cài Spark Cluster nhưng do mình đã cài Hadoop nên mình sẽ sử dụng YARN của Hadoop để lập lịch cho job.

# 1. Download Spark
Lên trang chủ của Spark để tải về: 
https://spark.apache.org/downloads.html
Nên chọn phiên bản Spark nào tương thích với Hadoop đã cài, ở mình dùng bản Spark 3.x

# 2. Cấu hình môi trường
Vào file `.bashrc` và thêm vào như sau:
```
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin
```

Để áp dụng các thay đổi (nhớ chạy câu lệnh dưới mỗi khi thay đổi file `bashrc`)
```
source ~/.bashrc
```

# 3. Config

Config file `spark-env.sh`:
```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export SPARK_HOME=/opt/spark
export HADOOP_HOME=/opt/hadoop

export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export YARN_CONF_DIR=$HADOOP_HOME/etc/hadoop
export SPARK_LOG_DIR=$SPARK_HOME/logs

```

Config file `spark-defaults.conf`:
```
spark.eventLog.enabled           true
spark.eventLog.dir               hdfs://m1:8020/spark-event-logs
spark.history.fs.logDirectory    hdfs://m1:8020/spark-event-logs
spark.eventLog.logBlockUpdates.enabled  true
```
Nếu không chạy `spark history server` thì k cần config cái này

# 4. Chạy Spark History Server
Tạo file spark-history-server.service để khởi chạy tự động:
```
[Unit]
Description=Run service as user phukaioh
DefaultDependencies=no

[Service]
Type=simple
User=phukaioh
Group=phukaioh
ExecStart=/opt/spark/sbin/start-history-server.sh
ExecStop=/opt/spark/sbin/stop-history-server.sh
TimeoutStartSec=0
RemainAfterExit=yes

[Install]
WantedBy=default.target
```

Khởi chạy:
```
./sbin/start-history-server.sh
```

![image](https://user-images.githubusercontent.com/81508954/201504631-c1cd2ca3-4c52-4a59-89a0-53062bd2fbf8.png)


# 5. Run job on Yarn
Chạy 1 job example như sau:
```
./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn --deploy-mode cluster examples/jars/spark-examples*.jar 10
```
Trên Yarn sẽ xuất hiện 1 job: 
![image](https://user-images.githubusercontent.com/81508954/201504593-455a10ff-cc97-4f2c-b8dc-d4a650baf537.png)

UI job:
![image](https://user-images.githubusercontent.com/81508954/201504637-0e4d00ff-d719-4c0c-b598-b17701203021.png)


