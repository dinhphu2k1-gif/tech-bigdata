HBASE
===

# 1. Điều kiện
Đã cài đặt Hadoop và Zookeeper

# 2. Download Hbase
Download tại trang chủ của Apache Hbase:
https://hbase.apache.org/downloads.html

Nên chọn bản Hbase 2.x để tương thích với Hadoop 3.x

# 3. Config Hbase

## 3.1. Khởi tạo biến môi trường
Thêm vào file `.bashrc`:

```
export HBASE_HOME=/opt/hbase
export PATH=$PATH:$HBASE_HOME/bin
```

Sau đó : 
```
source ~/.bashrc
```

## 3.2. Config hbase-site.xml
```
<configuration>
    <property>
        <name>hbase.cluster.distributed</name>
        <value>true</value>
    </property>
    <property>
        <name>hbase.tmp.dir</name>
        <value>/opt/hbase/tmp</value>
    </property>
    <property >
        <name>hbase.rootdir</name>
        <value>hdfs://m1:8020/hbase</value>
    </property>
    <property>
        <name>hbase.zookeeper.quorum</name>
        <!--Ideally we should be using a hostname here instead of IP address. Please refer to
        https://issues.apache.org/jira/browse/HBASE-23764 for why we switched to IP address. Should be
        changed once we fix the underlying ZK issue.-->
        <value>m1,m2</value>
    </property>
    <property>
        <name>hbase.zookeeper.property.dataDir</name>
        <value>/opt/zookeeper/tmp</value>
        <description>Property from ZooKeeper's config zoo.cfg.
        The directory where the snapshot is stored.</description>
    </property>
</configuration>
```
## 3.3. Config hbase-env.sh
```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export HBASE_MANAGES_ZK=false
```

## 3.4. Config Regionservers
```
m1
m2
```

# 4. Khởi chạy

## 4.1. Service
```
Description=Run service as user phukaioh
DefaultDependencies=no

[Service]
Type=simple
User=phukaioh
Group=phukaioh
ExecStart=/opt/hbase/bin/start-hbase.sh
ExecStop=/opt/hbase/bin/stop-hbase.sh  
TimeoutStartSec=0
RemainAfterExit=yes

[Install]
WantedBy=default.target
```

## 4.2. Start service
```
./bin/start-hbase.sh
```

UI: 
![image](https://user-images.githubusercontent.com/81508954/201505309-93bab578-87df-4330-a91d-220a2aca4147.png)

