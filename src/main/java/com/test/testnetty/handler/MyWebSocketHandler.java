package com.test.testnetty.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * websocket处理
 *
 * @author zhaoy
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
//        System.out.println(JSON.parse(ctx.channel().toString()));
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
            logger.info("接收到客户端消息：" + text);
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
        logger.info("读取数据完成调用：" + ctx.channel().remoteAddress());
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
        logger.info("用户自定义事件：" + ctx.channel().remoteAddress());
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
