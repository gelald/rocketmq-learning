package com.github.gelald.rocketmq.producer.starter;


import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author WuYingBin
 * date: 2022/8/26
 */
public class MessagesSplitter implements Iterator<List<Message<String>>> {
    private final int MAX_SIZE = 1024 * 1024 * 4;
    private final int LOG_SIZE = 20;
    private final List<Message<String>> messages;
    private int currentIndex = 0;

    public MessagesSplitter(List<Message<String>> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < messages.size();
    }

    @Override
    public List<Message<String>> next() {
        int startIndex = getStartIndex();
        int nextIndex = startIndex;
        int totalSize = 0;
        while (nextIndex < messages.size()) {
            Message<String> message = messages.get(nextIndex);
            // 计算当前消息的长度
            int singleMessageSize = calcMessageTotalSize(message);
            // 只要消息还没超出长度限制就一直往后累计直到达到消息长度限制
            if (singleMessageSize + totalSize > MAX_SIZE) {
                break;
            } else {
                totalSize += singleMessageSize;
            }
            nextIndex++;
        }
        // 提取子集合
        List<Message<String>> subList = messages.subList(startIndex, nextIndex);
        currentIndex = nextIndex;
        return subList;
    }

    // 计算一个消息的尺寸
    private int calcMessageTotalSize(Message<String> message) {
        int size = message.getPayload().length();
        MessageHeaders headers = message.getHeaders();
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            Object value = entry.getValue();
            // 演示 简单处理
            if (value instanceof String) {
                size += entry.getKey().length();
                size += ((String) value).length();
            }
        }
        size += LOG_SIZE;
        return size;
    }

    // 获取下一个应该取的索引
    private int getStartIndex() {
        // 先获取当前集合第一个消息的长度
        Message<String> currentMessage = messages.get(currentIndex);
        int currentMessageSize = calcMessageTotalSize(currentMessage);
        while (currentMessageSize > MAX_SIZE) {
            // 如果这个消息的长度本就大于消息长度限制
            // 那么就取下一个消息，直到消息长度小于长度限制
            currentIndex += 1;
            currentMessage = messages.get(currentIndex);
            currentMessageSize = calcMessageTotalSize(currentMessage);
        }
        return currentIndex;
    }
}
