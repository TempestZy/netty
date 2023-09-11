package com.test.testnetty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * websocket处理
 *
 * @author tempest
 * @date 2023-09-07 15:22:34
 */
public class MyWebSocketHandler extends ChannelInboundHandlerAdapter {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    /**
     * 初始化channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    /**
     * 注销channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    /**
     * 建立连接事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 加入通道组
        String clientId = ctx.channel().id().toString();
        SendHandler.CHANNELS.add(ctx.channel());
        SendHandler.CHANNEL_MAP.put(clientId, ctx.channel());
        SendHandler.addChannel(ctx.channel());
        ctx.fireChannelActive();
        logger.info("建立连接: " + ctx.channel().remoteAddress());
    }

    /**
     * 连接关闭
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SendHandler.CHANNEL_MAP.remove(ctx.channel().id().toString());
        SendHandler.CHANNELS.remove(ctx.channel());
        logger.info("断开连接：" + ctx.channel().remoteAddress());
        SendHandler.removeChannel(ctx.channel());
        super.channelInactive(ctx);
    }

    /**
     * 监听客户端消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            String text = textFrame.text();

            if (text.contains("heartbeat")) {
                logger.info("接收到客户端心跳消息：" + text + ",ip:" + ctx.channel().remoteAddress());
                SendHandler.sendServerMessage(ctx, "收到心跳");
            } else if (text.contains("broadcast")) {
                logger.info("接收到发送广播消息的通知：" + text);
                SendHandler.broadcastMessage("这是一条广播消息:" + text);
            } else {
                SendHandler.sendServerMessage(ctx, "收到前端推送消息：" + text);
            }

            // 处理接收到的消息
            // 这里可以将消息广播给所有连接的客户端等等
        }
    }

    /**
     * 读取数据完成调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        logger.info("读取数据完成：" + ctx.channel().remoteAddress());
        super.channelReadComplete(ctx);
    }

    /**
     * 用户自定义事件（心跳检测也可放在这里）
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        logger.info("用户自定义事件：" + ctx.channel().remoteAddress());

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
                default:
                    break;
            }
            logger.info(ctx.channel().remoteAddress() + " 发生了超时事件：" + eventType);
        }

        super.userEventTriggered(ctx, evt);
    }

    /**
     * 通知通道变化
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("通知通道变化：" + ctx.channel().remoteAddress());
        super.channelWritabilityChanged(ctx);
    }

    /**
     * 监听异常事件
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("netty异常：" + cause.getMessage());
        // 处理异常
//        cause.printStackTrace();
        ctx.close();
    }
}
