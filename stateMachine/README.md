# 轻量级状态机

* 纯java实现轻量级状态机，不依赖任何中间件、spring

## Maven依赖

```xml
<dependency>
    <groupId>com.wsq.library</groupId>
    <artifactId>stateMachine</artifactId>
</dependency>
```

## 配置

* 编写配置类（继承AbstractConfig），示例在下面
* 在resources下建立SPI(META-INF/services)，并配置响应的自定义配置

## 使用示例

```java
public class Main {
    public static void main(String[] args) {
        StateMachine<StateEnum, EventEnum> machine = new StateMachine<>("wsq", StateEnum.INIT);

        ActionResult publish = machine.publish(new Event<>(EventEnum.PASS));

        System.out.println();
    }

    @AllArgsConstructor
    public enum EventEnum {
        PASS(1),
        UN_PASS(2);

        public int id;
    }

    @AllArgsConstructor
    public enum StateEnum {
        INIT(1),
        SUCCESS(2),
        FAIL(3);

        public int id;
    }

    public static class Config extends AbstractConfig<StateEnum, EventEnum> {

        @Override
        protected AbstractConfig<StateEnum, EventEnum> build() {
            return stateMachineName("wsq").
                    source(StateEnum.INIT).
                    event(EventEnum.PASS).
                    target(StateEnum.SUCCESS).
                    choise(event -> true).
                    yes(new DefaultAction<>()).
                    no(null).
                    end();
        }
    }
}
```