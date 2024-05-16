ELASTICSEARCH
===

# 1. Cấu hình biến môi trường
```
export ELASTICSEARCH_HOME=/opt/elasticsearch
export PATH=$PATH:$ELASTICSEARCH_HOME/bin

export ES_JAVA_HOME=/opt/elasticsearch/jdk
```

Do dùng jdk bên ngoài sẽ bị lỗi nên sẽ dùng sẵn jdk cung cấp trong elasticsearch

# 2. Config

## 2.1. Cấu hình file elasticsearch.yml
```
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
cluster.name: elasticsearch
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
node.name: node-1
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
path.data: /opt/elasticsearch/data
#
# Path to log files:
#
path.logs: /opt/elasticsearch/logs
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: m1
#
# Set a custom port for HTTP:
#
#http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when this node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
discovery.seed_hosts: [m1, m2]
#
# Bootstrap the cluster using an initial set of master-eligible nodes:
#
cluster.initial_master_nodes: ["node-2"]
#
# For more information, consult the discovery and cluster formation module documentation.
#
# ---------------------------------- Gateway -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
#
#gateway.recover_after_nodes: 3
#
# For more information, consult the gateway module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true
```

# 2.2. Cấu hình jvm.options
Nên giảm tài nguyên cho jvm để đỡ tốn ram
```
-Xms128m
-Xmx128m
```

# 3. Khởi chạy
## 3.1. Service
```
Description=Run service as user phukaioh
DefaultDependencies=no

[Service]
Type=simple
User=phukaioh
Group=phukaioh
ExecStart=/opt/elasticsearch/bin/elasticsearch
TimeoutStartSec=0
RemainAfterExit=yes

[Install]
WantedBy=default.target
```
## 3.2. Run service
```
./bin/elasticsearch
```

# 4. Kiểm tra
Vào trình duyệt ```m1:9200``` để check elasticsearch đã chạy hay chưa

![image](https://user-images.githubusercontent.com/81508954/201514813-1cfa2863-f4b3-447e-9a76-e268f0c3267d.png)
