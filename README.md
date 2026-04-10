# Local Run

이 프로젝트는 톰캣이 필요 없습니다. `org.example.Main`이 자바 내장 HTTP 서버로 바로 실행됩니다.

## 필요한 것

- JDK 8 이상
- `JAVA_HOME` 설정

## 가장 쉬운 실행

IntelliJ에서 [src/main/java/org/example/Main.java](/C:/Users/Administrator/IdeaProjects/test/src/main/java/org/example/Main.java) 를 실행하거나,
Windows에서는 아래 파일을 실행하면 됩니다.

```bat
run-local.bat
```

실행 후 브라우저에서 `http://localhost:8080` 으로 접속하면 됩니다.

## 참고

- Maven 없어도 됩니다.
- 톰캣 없어도 됩니다.
- `8080` 포트가 이미 사용 중이면 실행되지 않습니다.
