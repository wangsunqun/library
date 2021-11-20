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
![Image text](https://github.com/wangsunqun/library/blob/master/stateMachine/src/main/resources/picture/1.png)

```
 public static void main(String[] args) {
        StateMachine<StateEnum, EventEnum> machine = new StateMachine<>("wsq", BLANK_FORM);

        ActionResult publish1 = machine.publish(new Event<>(WRITE));
        if (Objects.nonNull(publish1))
        System.out.println("1发布write事件，结果: 当前状态: " + publish1.getNextState().name() + ", 返回: " + publish1.getData());

        ActionResult publish2 = machine.publish(new Event<>(CHECK));
        if (Objects.nonNull(publish2))
        System.out.println("2发布check事件，结果: 当前状态: " + publish2.getNextState().name() + ", 返回: " + publish2.getData());


        ActionResult publish3 = machine.publish(new Event<>(DEAL));
        if (Objects.nonNull(publish3))
        System.out.println("3发布deal事件，结果: 当前状态: " + publish3.getNextState().name() + ", 返回: " + publish3.getData());


        ActionResult publish4 = machine.publish(new Event<>(SUBMIT));
        if (Objects.nonNull(publish4))
        System.out.println("4发布submit事件，结果: 当前状态: " + publish4.getNextState().name() + ", 返回: " + publish4.getData());


        ActionResult publish5 = machine.publish(new Event<>(CHECK));
        if (Objects.nonNull(publish5))
        System.out.println("5发布check事件，结果: 当前状态: " + publish5.getNextState().name() + ", 返回: " + publish5.getData());

        ActionResult publish6 = machine.publish(new Event<>(SUBMIT));
        if (Objects.nonNull(publish6))
        System.out.println("6发布submit事件，结果: 当前状态: " + publish6.getNextState().name() + ", 返回: " + publish6.getData());

        System.out.println();
    }

    @AllArgsConstructor
    public enum EventEnum {
        WRITE, // 填写
        CHECK,//校验
        DEAL,//处理
        SUBMIT // 提交
    }

    @AllArgsConstructor
    public enum StateEnum {
        BLANK_FORM, // 空白表单
        FULL_FORM, // 填写完表单
        DEAL_FORM,//待处理表单
        CONFIRM_FORM, // 校验完表单
        SUCCESS_FORM,// 成功表单
        FAILED_FORM//失败表单
    }

    int i = 0;
    public boolean check() {
        if (i == 0) {
            i++;
            return false;
        }
        return true;
    }

    public boolean deal() {
        return true;
    }

    public static class StateMachineConfig extends AbstractConfig<StateEnum, EventEnum> {
        Main m = new Main();

        @Override
        public AbstractConfig<StateEnum, EventEnum> build() {
            return stateMachineName("wsq").
                    transition(BLANK_FORM, WRITE, FULL_FORM, new DefaultAction<StateEnum, EventEnum>() {
                        @Override
                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
                            return "blank_form -> write -> full_form";
                        }
                    }).
                    choice(FULL_FORM, CHECK, m::check).
                    yes(CONFIRM_FORM, new DefaultAction<StateEnum, EventEnum>() {
                        @Override
                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
                            return "full_form -> check -> confirm_form";
                        }
                    }).
                    no(DEAL_FORM, new DefaultAction<StateEnum, EventEnum>() {
                        @Override
                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
                            return "full_form -> check -> deal_form";
                        }
                    }).
                    end().
                    transition(CONFIRM_FORM, SUBMIT, SUCCESS_FORM, new DefaultAction<StateEnum, EventEnum>() {
                        @Override
                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
                            return "confirm_form -> submit -> success_form";
                        }
                    }).
                    choice(DEAL_FORM, DEAL, m::deal).
                    yes(FULL_FORM, new DefaultAction<StateEnum, EventEnum>() {
                        @Override
                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
                            return "deal_form -> deal -> full_form";
                        }
                    }).
                    no(FAILED_FORM, new DefaultAction<StateEnum, EventEnum>() {
                        @Override
                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
                            return "deal_form -> deal -> failed_form";
                        }
                    }).
                    end();
        }
    }
```