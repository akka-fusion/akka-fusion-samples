# sample-scheduler-job

## 特性

- 使用Circe作为JSON工具
- 基于Quartz实现作业调度
- 基于Akka Cluster实现调度控制的集群化，高可用
- 同时提供RESTful和Grpc两套API接口

## 使用

### Install PostgreSQL for Docker

```shell script
docker build -t sample-postgres . 
docker run -p 55432:5432 --name sample-postgres -d sample-postgres 
```

### Init database tables

```sbtshell
> sample-scheduler-job/testOnly sample.scheduler.InitTablesTest
```

### Unit test

```sbtshell
> sample-scheduler-job/testOnly sample.scheduler.InitTablesTest
```
