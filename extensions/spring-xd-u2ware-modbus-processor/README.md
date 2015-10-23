# u2ware-modbus-processor

u2ware-modbus-processor 는 Modbus 연동을 위한 [Processor Type Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 입니다. 아래와 같은 옵션이 있습니다.

|option|description|default|
|---|---|---|
|host|연결하고자하는 원격 Modbus 디바이스의 IP Address 입니다.| |
|port|연결하고자하는 원격 Modbus 디바이스의 Port 입니다.| |
|jsonInput|Modbus 요청메세지로 JSON 문자열을 허용할 지  여부를 결정합니다.|true|
|split|Modbus 응답메세지는 List 이므로 이를 분할 할지 여부를 결정합니다.|true|
|jsonOutput|Modbus 응답메세지를 JSON 으로 변환 할지 여부를 결정합니다.|true|

# Usage

다음과 같이 Stream 을 생성합니다.
```
xd:> stream create --name "myModbusProcessor" --definition "http | u2ware-modbus-processor --host=127.0.0.1 --port=10502 | log"
```

Stream 을 배포합니다.
```
xd:> stream deploy --name "myModbusProcessor"
```

http 명령어를 이용하여, Spring XD console 에서 Modbus 응답 JSON 문자열을 볼 수 있습니다.
```
xd:> http post --target http://localhost:9000 --data "{\"unitId\":0,\"functionCode\":4,\"offset\":0,\"count\":6}"
```

[io.github.u2ware.modbus.core.ModbusRequest]() 객체로 변환가능한 JSON 문자열을 전달합니다.  

* unitId: Modbus 요청 패킷의 unitId 값입니다.
* functionCode: Modbus 요청 패킷의 functionCode 값입니다.
* offset: Modbus 요청 패킷의 offset 값입니다.
* count: Modbus 요청 패킷의 count 값입니다.

Stream 을 삭제합니다.
```
xd:> stream destroy --name "myModbusProcessor"
```



