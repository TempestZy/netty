package com.test.testnetty.config;

import com.test.testnetty.handler.MyWebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * netty配置
 *
 * @author tempest
 * @date 2023-09-07 15:21:11
 */

@Configuration
public class NettyConfig {

    private static final Logger logger = LoggerFactory.getLogger(NettyConfig.class);

    /**
     * 创建线程池组，处理客户端的连接
     *
     * @return 异步事件循环
     */
    @Bean
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup(1);
    }

    /**
     * 创建线程池组，处理客户端的读写
     *
     * @return 异步事件循环
     */
    @Bean
    public EventLoopGroup workerGroup() {
        return new NioEventLoopGroup(8);
    }

    /**
     * netty服务引导
     *
     * @return netty
     */
    @Bean
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                // 日志处理
                .handler(new LoggingHandler(LogLevel.INFO))
                // 配置链式解码器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // 加入http的编码和解码器
                                .addLast(new HttpServerCodec())
                                // 浏览器向服务器发消息是以块发送的，添加 ChunkedWriteHandler 处理器
                                .addLast(new ChunkedWriteHandler())
                                // 解码成FullHttpRequest
                                .addLast(new HttpObjectAggregator(1024 * 10))
                                // 心跳检测机制 读，写，读写
                                .addLast(new IdleStateHandler(3, 4, 5, TimeUnit.MINUTES))
                                // 添加WebSocket解编码
                                .addLast(new WebSocketServerProtocolHandler("/websocket"))
                                // 自定义 handler，处理业务逻辑
                                .addLast(new MyWebSocketHandler());
                    }
                });
        return serverBootstrap;
    }

    /**
     * 异步绑定端口
     *
     * @param serverBootstrap 服务引导
     * @return 异步管道
     */
    @Bean
    public ChannelFuture nettyChannelFuture(ServerBootstrap serverBootstrap) {
        ChannelFuture channelFuture = serverBootstrap.bind(18011).syncUninterruptibly();
        channelFuture.addListener((ChannelFutureListener) c -> {
            if (c.isSuccess()) {
                logger.info("netty 启动成功");
            } else {
                logger.info("netty 启动失败");
            }
        });
        return channelFuture;
    }
}
