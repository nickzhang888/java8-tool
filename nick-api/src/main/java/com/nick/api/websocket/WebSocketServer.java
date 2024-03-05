package com.nick.api.websocket;

import com.nick.common.utils.ip.IpUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 纪淮永
 * @ClassName: WebSocketServer
 * @Description: webSocket服务初始化
 * @date 2021年4月27日 上午10:25:01
 */
@Slf4j
@Component
public class WebSocketServer {

    @Autowired
    private com.nick.api.modules.websocket.WebSocketServerHandler webSocketServerHandler;

    @Value("${netty.websocket.enable}")
    private Boolean websocketEnable;

    @Value("${netty.websocket.port}")
    private Integer websocketPort;

    @Value("${netty.websocket.path}")
    private String websocketPath;

    @PostConstruct()
    public void init() {
        // 需要开启一个新的线程来执行netty server 服务器
        if (websocketEnable) {
            new Thread(() -> {
                startServer();
            }).start();
        }
    }

    /**
     * @throws Exception
     * @Description: 启动webSocket服务
     * @author 纪淮永
     */
    private void startServer() {
        // 配置服务端NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup) // //配置主从线程组
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .option(ChannelOption.SO_BACKLOG, 1024) // 配置一些TCP的参数
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // websocket协议本身是基于http协议的
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            //聚合器，使用websocket会用到
                            socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
                            // 以块的方式来写的处理器
                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                            // 优先加载Handler解决URL传参断开连接
                            socketChannel.pipeline().addLast(webSocketServerHandler);
                            socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler(websocketPath, null, true, 65536));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(websocketPort).sync(); // 服务器异步创建绑定
            log.info("webSocket初始化成功,连接地址  ws://{}:{}{}", IpUtils.getHostIp(), websocketPort, websocketPath);
            channelFuture.channel().closeFuture().sync(); // 关闭服务器通道
        } catch (InterruptedException e) {
            log.error("webSocket初始化异常:{}", e.getMessage());
        } finally {
            // netty优雅停机
            try {
                bossGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                log.error("netty停止异常:{}", e.getMessage());
            }
            try {
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                log.error("netty停止异常:{}", e.getMessage());
            }
        }
    }
}
