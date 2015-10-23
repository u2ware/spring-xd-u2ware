# u2ware-mongodb-ext-sink

u2ware-mongodb-ext-sink 는 MongoDB 데이터 저장을 위한 [Sink Type Module](http://docs.spring.io/spring-xd/docs/1.2.1.RELEASE/reference/html/#modules) 입니다. 아래와 같은 옵션이 있습니다.

|option|description|default|
|---|---|---|
|host|연결하고자하는 MongoDB 의 IP Address 입니다. | |
|port|연결하고자하는 MongoDB 의 Port 입니다.|27017|
|databaseName|저장되는 MongoDB Database Name 입니다.| |
|idExpression|id 에 해당하는 값을 동적으로 얻기 위한 [SpEL](http://docs.spring.io/spring-integration/docs/4.2.0.RELEASE/reference/html/spel.html) 문자열입니다.||
|valueExpression|value 에 해당하는 값을 동적으로 얻기 위한 [SpEL](http://docs.spring.io/spring-integration/docs/4.2.0.RELEASE/reference/html/spel.html) 문자열입니다.||


# Usage

다음과 같이 Stream 을 생성합니다.
```
xd:> stream create --name "myMongodbSink" --definition "http | json-to-tuple | u2ware-mongodb-ext-sink --host=127.0.0.1  --databaseName=myDb --idExpression=payload.id  --valueExpression=payload.value"
```

Stream 을 배포합니다. 
```
xd:> stream deploy --name "myMongodbSink"
```

http 명령어를 이용하여, MongoDB에 데이터를 저장 할 수 있습니다.
```
xd:> http post --target http://localhost:9000 --data '{"id":"Joe","value":"26","something":"oops"}'
xd:> http post --target http://localhost:9000 --data '{"id":"Joe","value":"28","something":"oops"}'
```

Stream 을 삭제합니다.
```
xd:> stream destroy --name "myMongodbSink"
```



