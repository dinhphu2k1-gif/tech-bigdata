HADOOP
===
Cách cài hadoop cluster

# 1. Điều kiện
- Hệ điều hành Linux hoặc máy ảo Linux 
- JDK: ở đây mình dùng jdk 8, có 2 cách để cài JDK:
  - sử dụng lệnh của ubuntu: ```sudo apt-get install openjdk-8-jdk```
  - download file JDK từ oracle, giải nén và thêm vào file ```.bashrc``` đường dẫn để jdk
- SSH client và SSH server: ```sudo apt-get install openssh-server openssh-client -y```
- ssh key: ```ssh-keygen -t rsa```

# 2. Tạo máy ảo
Ở đây do không có máy vật lý nên sẽ dùng máy ảo để thay thế. Máy ai khỏe thì cho nó nhiều tài nguyên 1 chút để sau này dễ chạy job. Ít tài nguyên quá sẽ k đủ tài nguyên để chạy 1 job.

***Cấu hình***: 2 máy ảo (m1, m2) - ```4GB ram và 4->6 cores ```

Có thể dùng **Virtual Box** hoặc **VMWare** tùy các bạn, thậm chí là có thể dùng **docker** nhưng  mình k biết cài như nào

Nên tải bản **live server** thay vì **desktop** để tiết kiệm tài nguyên.

# 3. Download Hadoop
Tải 1 phiên bản Hadoop trên trang chủ của Hadoop: 
https://hadoop.apache.org/releases.html

Tải bản **binary** trong phần **Binary Download**
Sau đó giải nén bằng lệnh:
```
tar xvzf hadoop-3.2.2.tar.gz
```

# 4. Cài đặt

## 4.1. Cấp quyền truy cập
Sau khi đã tạo được ssh-key trên các máy thì phải copy các máy khác để có quyền truy cập giữa các máy
```
ssh-copy-id username@ip
```

**Lưu ý**: 
  - các máy nên có cùng username để dễ truy cập
  - có thể cấu hình địa chỉ ip ở ```/etc/hosts``` để dùng tên thay thế thay vì phải nhớ địa chỉ ip

## 4.2. Cấu hình biến mỗi trường
Cấu hình ở file ```~/.bashrc ``` và thêm các biến sau vào cuối file:

```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64

export HADOOP_HOME=/opt/hadoop
export PATH=$PATH:$HADOOP_HOME/bin
```
Áp dụng các thay đổi bằng lệnh sau: ```source ~/.bashrc```

**Lưu ý**: hadoop có thể để ở đâu nếu muốn, ở đây để folder `/etc`

## 4.3. Chỉnh sửa file hadoop-env
Mở file `hadoop-env.sh` bằng lệnh sau:
```
sudo nano $HADOOP_HOME/etc/hadoop/hadoop-env.sh
```

Và sửa lại 2 biến `JAVA_HOME` và `HADOOP_HOME`:
```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export HADOOP_HOME=/opt/hadoop
```

## 4.4. Chỉnh sửa file core-site.xml
Thêm vào giữa 2 thẻ `configuration` để được nội dung như sau :
```
<configuration>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/opt/hadoop/tmp</value>
    </property>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://master:8020</value>
    </property>

</configuration>
```
Ở đây `master` là `m1` nên thay bằng
```
<value>hdfs://m1:8020</value>
```

## 4.5. Chỉnh sửa file hdfs-site.xml
```
<configuration>
    <property>
        <name>dfs.datanode.name.dir</name>
        <value>/opt/hadoop/dfs/namenode</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name> 
        <value>/opt/hadoop/dfs/datanode</value>
    </property>
    <property>
        <name>dfs.replication</name> 
        <value>2</value>
    </property>
</configuration>

```

Nhớ tạo folder `dfs/namenode` để sau format namenode

## 4.6. Chỉnh sửa file mapred-site.xml
```
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
```

## 4.7. Chỉnh sửa file yarn-site.xml
```
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services.mapreduce.shuffle.class</name>
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>
    </property>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>m1</value>
    </property>
    <property>
        <name>yarn.scheduler.minimum-allocation-mb</name>
        <value>512</value>
    </property>
    <property>
        <name>yarn.scheduler.maximum-allocation-mb</name>
        <value>2048</value>
    </property>
    <property>
        <name>yarn.nodemanager.resource.memory-mb</name>
        <value>2048</value>
    </property>
    <property>
        <name>yarn.nodemanager.resource.cpu-vcores</name>
        <value>3</value>
    </property>
    <property>
        <name>yarn.scheduler.maximum-allocation-vcores</name>
        <value>2</value>
    </property>
</configuration>
```

Tùy cấu hình máy ảo để cấp tài nguyên cho hadoop

## 4.8. Chỉnh sửa file capacity-scheduler.xml
Sửa thuộc tính `yarn.scheduler.capacity.maximum-am-resource-percent` từ `0.1` -> `1` để có thể sử dụng hết tài nguyên của hadoop

## 4.9. Chỉnh sửa file workers
Để xác định `datanote` chạy trên máy nào:

![image](https://user-images.githubusercontent.com/81508954/201503649-e68c0c34-a6bb-4a1a-8fb1-5cc898a24ea7.png)

## 4.10. Format Namenode
Cần định dạng lại namenode trước khi khởi chạy:
```
hdfs namenode -format
```

**Lưu ý**: đối với máy 2 cũng làm tương tự, có thể dùng lệnh scp để copy folder cho nhanh

# 5. Khởi chạy
Vào thư mục sbin:
```
./start-all.sh
```
Sau khi khởi chạy, dùng lệnh `jps` để kiểm tra:

![image](https://user-images.githubusercontent.com/81508954/201503777-7b6509ce-bfa7-4167-a362-a36f1454e82b.png)

Có thể viết file serivce để tự động viêc khởi chạy hadoop khi bật máy ảo:
- Vào folder `/etc/systemd/system` và tạo file `hadoop.service`:

```
sudo nano hadoop.service
```
- Dán vào file như sau:
```
[Unit]
Description=Run service as user phukaioh
DefaultDependencies=no

[Service]
Type=simple
User=phukaioh
Group=phukaioh
ExecStart=/opt/hadoop/sbin/start-all.sh
ExecStop=/opt/hadoop/sbin/stop-all.sh
TimeoutStartSec=0
RemainAfterExit=yes

[Install]
WantedBy=default.target
```
- Reload deamon service:
```
sudo systemctl daemon-reload
```

- Service tự khỏi chạy mỗi khi bật máy
```
sudo systemctl enable example.service
```

Đọc thêm bài sau để hiểu rõ: https://www.shubhamdipt.com/blog/how-to-create-a-systemd-service-in-linux/
# 6. Truy cập UI
Namenode:
![image](https://user-images.githubusercontent.com/81508954/201503989-a93dea06-5cba-4519-861e-91ed23de0625.png)

Yarn: 
![image](https://user-images.githubusercontent.com/81508954/201503999-d32c476a-5ce4-4cc6-a002-934ad571c9e9.png)
![image](https://user-images.githubusercontent.com/81508954/201504015-1d96ba41-28ab-415f-b4c2-7f0de1a14cc8.png)
