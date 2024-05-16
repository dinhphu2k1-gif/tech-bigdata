JANUSGRAPH
===

# 1. Điều kiện
- Đã cái Hbase để lm backend storage. Có thể sửa dụng Hive hoặc các DB khác tùy yêu cầu
- Đã cài Elasticsearch để lm index storage.

# 2. Cấu hình
Thêm 3 file:
- `janus-hbase-es.properties` vào thư mục `./conf`
- `gremlin-server-hbase-es.yml` vào thư mục `./conf/gremlin-server`
- `schema.groovy` vào thư mục `./scripts`

# 3. Build schema
```
./bin/gremlin.sh -e scripts/schema.groovy conf/janus-hbase-es.properties
```

# 4. Run service
```
cd /opt/janusgraph-0.6.2/ && \
bin/janusgraph-server.sh start conf/gremlin-server/gremlin-server-hbase-es.yml
```
