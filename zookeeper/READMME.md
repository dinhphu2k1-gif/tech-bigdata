ZOOKEEPER
===

# 1. Config file zoo.cfg
```
# The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial 
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
dataDir=/opt/zookeeper/tmp
# the port at which the clients will connect
clientPort=2181
# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# https://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1

## Metrics Providers
#
# https://prometheus.io Metrics Exporter
#metricsProvider.className=org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider
#metricsProvider.httpHost=0.0.0.0
#metricsProvider.httpPort=7000
#metricsProvider.exportJvmInfo=true
server.1=m1:2888:3888
server.2=m2:2888:3888
```

# 2. Tạo myid
Đến thư mục `dataDir` đã tạo trước đó, tạo file `myid`. Và gán `id` cho máy đó
Ví dụ: m1 gán `myid = 1`, m2 gán `myid = 2`

![image](https://user-images.githubusercontent.com/81508954/201512109-aaa0c8d3-2d99-4812-b309-1fa482e59a45.png)


# 3. Khởi chạy
# 3.1. Service
```
[Unit]
Description=Run service as user phukaioh
DefaultDependencies=no

[Service]
Type=simple
User=phukaioh
Group=phukaioh
ExecStart=/opt/zookeeper/bin/zkServer.sh start
ExecStop=/opt/zookeeper/bin/zkServer.sh stop
TimeoutStartSec=0
RemainAfterExit=yes

[Install]
WantedBy=default.target
```

# 3.2. Run service
```
/opt/zookeeper/bin/zkServer.sh start
```
