package com.wsq.library.gateway.plugin.test.handler;

import com.wsq.library.gateway.plugin.test.dto.TestRuleDto;
import org.apache.shenyu.common.dto.RuleData;
import org.apache.shenyu.common.utils.GsonUtils;
import org.apache.shenyu.plugin.base.handler.PluginDataHandler;

import java.util.Optional;

/**
 * 不一定要有，如果插件有自定义参数才用这个，主要作用就是实时接收处理admin的参数变化，并且自己缓存起来
 *
 * @author wsq
 * @date 2021/12/13 19:41
 */
public class TestPluginHandler implements PluginDataHandler {
    @Override
    public String pluginNamed() {
        return "test";
    }

    @Override
    public void handlerRule(RuleData ruleData) {
        Optional.ofNullable(ruleData.getHandle()).ifPresent(s -> {
            System.out.println(s);

            TestRuleDto testRuleDto = GsonUtils.getInstance().fromJson(s, TestRuleDto.class);
            TestRuleDto.CACHE.put(ruleData.getId(), testRuleDto);
        });
    }

    @Override
    public void removeRule(RuleData ruleData) {
        Optional.ofNullable(ruleData).ifPresent(s -> TestRuleDto.CACHE.remove(s.getId()));
    }
}
