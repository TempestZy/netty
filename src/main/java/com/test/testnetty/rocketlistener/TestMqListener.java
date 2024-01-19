package com.test.testnetty.rocketlistener;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 测试消息队列消费者
 *
 * @author zhaoy
 * @date 2024-01-11 16:28:07
 */
@Component
@RocketMQMessageListener(consumerGroup = "test_group", topic = "test_message",messageModel = MessageModel.BROADCASTING)
public class TestMqListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("2接收消息：" + s);
    }
}
