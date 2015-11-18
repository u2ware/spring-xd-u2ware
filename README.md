Spring XD U2ware
=================================================
# Introduction

각종 Custom Module 이 설치된 [spring-xd-1.2.1.RELEASE](https://repo.spring.io/libs-release/org/springframework/xd/spring-xd/1.2.1.RELEASE/spring-xd-1.2.1.RELEASE-dist.zip)의 재배포판입니다.
Spring XD 에 대해서는 [가이드](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#_install_spring_xd)를 참조 할 수 있니다.
 

# Download 

[spring-xd-u2ware-1.2.1.zip]()

# Run

임의로 지정한 경로(HOME)에 압축을 풀고, 다음과 같이 Spring XD 을 실행합니다.  
```
[HOME]/xd/bin>$ ./xd-singlenode
 _____                           __   _______
/  ___|          (-)             \ \ / /  _  \
\ `--. _ __  _ __ _ _ __   __ _   \ V /| | | |
 `--. \ '_ \| '__| | '_ \ / _` |  / ^ \| | | |
/\__/ / |_) | |  | | | | | (_| | / / \ \ |/ /
\____/| .__/|_|  |_|_| |_|\__, | \/   \/___/
      | |                  __/ |
      |_|                 |___/
1.2.1.RELEASE                    eXtreme Data


Started : SingleNodeApplication
Documentation: https://github.com/spring-projects/spring-xd/wiki
.
.
.
.
.
..... - Scheduling deployments to new container(s) in 15000 ms 
```


# Usage


다음과 같이 XD shell 을 실행하면 ```xd:>``` 로 진입합니다.
```
[HOME]/shell/bin>$ ./xd-shell
 _____                           __   _______
/  ___|          (-)             \ \ / /  _  \
\ `--. _ __  _ __ _ _ __   __ _   \ V /| | | |
 `--. \ '_ \| '__| | '_ \ / _` |  / ^ \| | | |
/\__/ / |_) | |  | | | | | (_| | / / \ \ |/ /
\____/| .__/|_|  |_|_| |_|\__, | \/   \/___/
      | |                  __/ |
      |_|                 |___/
eXtreme Data
1.2.1.RELEASE | Admin Server Target: http://localhost:9393
Welcome to the Spring XD shell. For assistance hit TAB or type "help".
xd:>
```

[Stream](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#streams) 이란, 이벤트 기반 데이터의 처리 경로에 대한 정의(definition) 입니다.
```xd:>``` 에서 [Stream](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#streams)을 생성(create)하고 배포(deploy)할 수 있습니다.

```
xd:>stream create --name "ticktock" --definition "time | log" 
Created new stream 'ticktock'

xd:>stream deploy --name "ticktock"
Deployed stream 'ticktock'
```

Spring XD 콘솔에서 다음과 같이 메세지가 출력됩니다.
```
2015-11-18T14:54:18+0900 1.2.1.RELEASE INFO task-scheduler-2 sink.ticktock - 2015-11-18 14:54:18
2015-11-18T14:54:19+0900 1.2.1.RELEASE INFO task-scheduler-2 sink.ticktock - 2015-11-18 14:54:19
2015-11-18T14:54:19+0900 1.2.1.RELEASE INFO task-scheduler-2 sink.ticktock - 2015-11-18 14:54:20
```

[Stream](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#streams) 은 여러가지 유형의 [Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 들을 사용하여 구성되며, [Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 은 그 유형에 따라, [Source](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#sources), [Processor](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#processors), [Sink](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#sinks) 등으로 분류 됩니다.

이벤트 기반 데이터는 1개의 [Source](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#sources) 에서 만들어 지고,  0개 또는 다수의 [Processor](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#processors)를 거쳐 0개 또는 1개의 [Sink](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#sinks)에서 처리되는 경로를 가지게 됩니다. 위 예제에서 'time' 은 [Source](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#sources) 이고, 'log' 는 [Sink](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#sinks) 입니다.
 

설치된 [Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 리스트를 아래 명령어로 볼 수 있습니다.
```
xd:>module list
      Source                                   Processor                              Sink                         Job
  ---------------------------------------  -------------------------------------  ---------------------------  -----------------
      file                                     aggregator                             aggregate-counter            filejdbc
      ftp                                      bridge                                 counter                      filepollhdfs
      gemfire                                  filter                                 field-value-counter          ftphdfs
      gemfire-cq                               http-client                            file                         gpload
      http                                     json-to-tuple                          ftp                          hdfsjdbc
      jdbc                                     object-to-json                         gauge                        hdfsmongodb
      jms                                      script                                 gemfire-json-server          jdbchdfs
      kafka                                    scripts                                gemfire-server               sparkapp
      mail                                     shell                                  gpfdist                      sqoop
      mongodb                                  splitter                               hdfs                         timestampfile
      mqtt                                     transform                              hdfs-dataset
      rabbit                                   u2ware-bacnet-processor                jdbc
      reactor-ip                               u2ware-http-server-processor           kafka
      reactor-syslog                           u2ware-hyundai-elevator-processor      log
      sftp                                     u2ware-modbus-processor                mail
      syslog-tcp                                                                      mongodb
      syslog-udp                                                                      mqtt
      tail                                                                            null
      tcp                                                                             rabbit
      tcp-client                                                                      redis
      time                                                                            rich-gauge
      trigger                                                                         router
      twittersearch                                                                   shell
      twitterstream                                                                   splunk
      u2ware-bacnet-rest-source                                                       tcp
      u2ware-bacnet-source                                                            throughput-sampler
      u2ware-http-server-source                                                       u2ware-mongodb-bas-sink
      u2ware-hyundai-elevator-rest-source
      u2ware-hyundai-elevator-source
      u2ware-modbus-rest-source
      u2ware-modbus-source
      u2ware-mongodb-rest-source
      u2ware-siemens-fireview-source
``` 
Spring XD 가 기본적으로 제공하는 Module 이외에 추가 설치된 Custom Modules 에 대해서 [다음](extensions/)을 참고 할 수 있습니다.