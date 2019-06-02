# fastR

本项目为一个简单的基于RSocket的RPC实现

1. 使用方法：引入依赖（还未发布到maven仓库

```xml
 <dependency>
    <groupId>me.lisirrx</groupId>
    <artifactId>fastR-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
 </dependency>
```
2. 启动center目录下的注册中心。

3. 服务端：
```java
@Service
@FastRSocketService
public class DemoServiceImpl implements DemoService {
    @Override
    @ServiceMethod
    public Mono<String> demo() {
        return Mono.just("Demo!");
    }
}

```

4. 客户端
```java
@Bean
public DemoService demoService(){
    return RemoteServiceBuilder.ofService(DemoService.class);
}
```

4. 客户端调用：

```java
demoService.demo()
    .subscribe(System.out::println);

// Demo!

```

TODO
- [ ] Zookeeper注册中心
- [ ] 重构启动流程，现在启动流程很混乱
- [x] 接入spring-boot-starter
- [ ] 重构并发相关，`publishOn`线程池和`TcpClient`/`TcpServer`线程池
- [ ] 整理依赖相关，抽离父pom
- [ ] 发布maven仓库
