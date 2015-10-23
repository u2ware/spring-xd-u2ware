# u2ware-bacnet-source

u2ware-bacnet-source 은 BACNet 연동을 위한 [Source Type Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 입니다. 아래와 같은 옵션이 있습니다.

|option|description|default|
|---|---|---|
|localPort|원격 BACNet 디바이스와 연결하는 로컬 포트 입니다.|47808|
|remoteAddress|연결하고자하는 원격 BACNet 디바이스의 Address 입니다. ```<ip>:<port>```| |
|remoteInstanceNumber|연결하고자하는 원격 BACNet 디바이스의 Instance Number입니다.| |
|fixedDelay|BACNet 응답메세지를 polling 하는 주기 입니다.(milliseconds).|10000|
|split|BACNet 응답메세지는 List 이므로 이를 분할 할지 여부를 결정합니다.|true|
|jsonOutput|BACNet 응답메세지를 JSON 으로 변환 할지 여부를 결정합니다.|true|

# Usage

다음과 같이 Stream 을 생성합니다.
```
xd:> stream create --name "myBacnetSource" --definition "u2ware-bacnet-source --remoteAddress=127.0.0.1:47809 --remoteInstanceNumber=47809 | log"
```

Stream 을 배포합니다. 10초 마다, Spring XD console 에서 BACNet 응답 JSON 문자열을 볼 수 있습니다.
```
xd:> stream deploy --name "myBacnetSource"
```

Stream 을 삭제합니다.
```
xd:> stream destroy --name "myBacnetSource"
```



