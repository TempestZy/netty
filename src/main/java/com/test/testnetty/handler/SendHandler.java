package com.test.testnetty.handler;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * SendHandler
 *
 * @author zhaoy
 * @date 2023-09-08 10:56:05
 */
public class SendHandler {

    /**
     * 存储所有连接的客户端通道
     */
    private static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存储分组信息和成员列表
     */
    private static final Map<String, ChannelGroup> GROUP_MAP = new HashMap<>();

    /**
     * 存储客户端标识和通道的映射
     */
    private static final Map<String, Channel> CHANNEL_MAP = new HashMap<>();

    /**
     * 添加客户端连接到通道组
     *
     * @param channel 通道
     */
    public static void addChannel(Channel channel) {
        CHANNELS.add(channel);
    }

    /**
     * 从通道组中移除客户端连接
     *
     * @param channel 通道
     */
    public static void removeChannel(Channel channel) {
        CHANNELS.remove(channel);
        // 从 clientMap 中移除对应的客户端标识
        CHANNEL_MAP.values().removeIf(existingChannel -> existingChannel == channel);
    }

    /**
     * 加入分组
     *
     * @param groupId 分组id
     * @param channel 通道
     */
    public static void joinGroup(String groupId, Channel channel) {
        ChannelGroup group = GROUP_MAP.computeIfAbsent(groupId, k -> new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
        group.add(channel);
    }

    /**
     * 离开分组
     *
     * @param groupId 分组id
     * @param channel 通道
     */
    public static void leaveGroup(String groupId, Channel channel) {
        ChannelGroup group = GROUP_MAP.get(groupId);
        if (group != null) {
            group.remove(channel);
        }
    }

    /**
     * 发送消息给指定客户端
     *
     * @param clientId 客户端id
     * @param message  消息
     */
    public static void sendToClient(String clientId, String message) {
        Channel channel = CHANNEL_MAP.get(clientId);
        if (channel != null) {
            channel.writeAndFlush(message);
        }
    }

    /**
     * 发送消息给指定分组
     *
     * @param groupId 分组id
     * @param message 消息
     */
    public static void sendToGroup(String groupId, String message) {
        ChannelGroup group = GROUP_MAP.get(groupId);
        if (group != null) {
            group.writeAndFlush(message);
        }
    }
}
