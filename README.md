Spring XD U2ware
=================================================
# Introduction

각종 Custom Module 이 설치된 [spring-xd-1.2.1.RELEASE](https://repo.spring.io/libs-release/org/springframework/xd/spring-xd/1.2.1.RELEASE/spring-xd-1.2.1.RELEASE-dist.zip) 기준의 재배포판입니다.
Spring XD 에 대해서는 [가이드](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#_install_spring_xd)를 참조합니다.
 

# Download 

[spring-xd-u2ware-1.2.1.zip]()

# Run

압축을 풀고, 다음 명령어를 실행하면 Spring XD 가 시작되고 메세지가 출력됩니다.
```
/xd/bin>$ ./xd-singlenode
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

Spring XD 를 실행한 후 다음 명령어로 XD shell 을 실행하면 ```xd:>``` 로 진입합니다.
```
/shell/bin>$ ./xd-shell
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
```xd:>``` 에서 설치된 [Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 리스트를 아래 명령어로 볼 수 있습니다.

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

# Usage

[Stream](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#streams) 을 등록(register)하고 배포(deploy)합니다.
추가된 Custom Modules 은 [다음](extensions/)을 참고합니다.









