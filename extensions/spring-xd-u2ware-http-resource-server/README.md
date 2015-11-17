# u2ware-http-server-source

u2ware-http-server-source 는 HTTP Static File Server 를 구성하기 위한 [Source Type Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 입니다. 아래와 같은 옵션이 있습니다.

|option|description|default|
|---|---|---|
|httpPort|구성하고자 하는 HTTP Server 의 로컬 포트 번호 입니다. | |
|resourceLocation| static file 경로 입니다.| |

# Usage

static page (file://Users/U2ware/Desktop/root/a/b/c.html) 를 작성합니다.
```
<html><body><h1>Hello World!!</h1></body></html>
```

다음과 같이 Stream 을 생성합니다.
```
xd:> stream create --name "myHttpFileServer" --definition "u2ware-http-server-source --httpPort=9999 --resourceLocation=file://Users/U2ware/Desktop/root"
```


Stream 을 배포합니다. 
```
xd:> stream deploy --name "myHttpFileServer"
```

http 명령어를 이용하여, 구성한 HTTP Server 의 응답 메세지를 확인 할 수 있습니다.
```
xd:> http get --target http://localhost:9999/a/b/c.html
> GET http://localhost:9999/a/b/c.html 
> 200 OK
<html><body><h1>Hello World!!</h1></body></html>
```

Stream 을 삭제합니다.
```
xd:> stream destroy --name "myHttpFileServer"
```
