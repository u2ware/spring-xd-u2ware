# u2ware-http-server-processor

u2ware-http-server-processor 는 HTTP Server 를 구성하기 위한 [Processor Type Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 입니다. 아래와 같은 옵션이 있습니다.

|option|description|default|
|---|---|---|
|httpPort|구성하고자 하는 HTTP Server 의 로컬 포트 번호 입니다. | |
|httpTimeout| HTTP 응답 시간 제한 값 입니다(milliseconds).| |

# Usage

다음과 같이 두 개의 Stream 을 생성합니다.
```
xd:> stream create --name "myHttpServer1" --definition "queue:myHttpReq > transform --expression=payload.toString().toUpperCase() > queue:myHttpRes"

xd:> stream create --name "myHttpServer2" --definition "queue:myHttpRes > u2ware-http-server-processor --httpPort=9999  --httpTimeout=10000 > queue:myHttpReq"
```

Stream 을 배포합니다. 
```
xd:> stream deploy --name "myHttpServer1"
xd:> stream deploy --name "myHttpServer2"
```

http 명령어를 이용하여, 구성한 HTTP Server 의 응답 메세지를 확인 할 수 있습니다.
```
xd:>http post --target http://localhost:9999 --data "hello world"
> POST (text/plain;Charset=UTF-8) http://localhost:9999 hello world
> 200 OK
HELLO WORLD
```



