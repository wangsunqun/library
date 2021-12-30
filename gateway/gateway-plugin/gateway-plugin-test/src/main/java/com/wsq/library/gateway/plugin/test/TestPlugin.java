package com.wsq.library.gateway.plugin.test;

import com.wsq.library.gateway.plugin.test.dto.TestRuleDto;
import org.apache.shenyu.common.dto.RuleData;
import org.apache.shenyu.common.dto.SelectorData;
import org.apache.shenyu.plugin.api.ShenyuPluginChain;
import org.apache.shenyu.plugin.base.AbstractShenyuPlugin;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author wsq
 * @Description
 * @date 2021/12/13 16:33
 */
public class TestPlugin extends AbstractShenyuPlugin {
    @Override
    public String named() {
        return "test";
    }

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, ShenyuPluginChain chain, SelectorData selector, RuleData rule) {
        TestRuleDto testRuleDto = TestRuleDto.CACHE.get(rule.getId());

        System.out.println("testRuleDto: " + testRuleDto.getFlag() + testRuleDto.getValue());

        return chain.execute(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
