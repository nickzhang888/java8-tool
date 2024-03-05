package com.nick.api.websocket;

import com.alibaba.fastjson.JSON;
import com.nick.api.constant.WebSocket;
import com.nick.api.modules.alarm.entity.AlarmEntity;
import com.nick.common.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Sharable
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    // 当前在线连接数
    private static int onlineCount = 0;

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 客户端与服务端建立连接时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    // 客户端与服务端断开连接时触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    // 接受客户端消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handleWebSocketFrame(ctx, msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 首次连接是FullHttpRequest，处理参数
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            // 如果url包含参数，需要处理
            if (uri.contains("?")) {
                String path = uri.substring(0, uri.indexOf("?"));
                request.setUri(path);
            }
            // 添加到ChannelGroup通道组
            boolean flag = channelGroup.add(ctx.channel());
            if (flag) {
                onlineCount++;
                log.info("与客户端建立连接成功，当前在线人数:{}", onlineCount);
            }
            // 自定义参数处理
            Map<String, String> paramMap = getParams(uri);
            String userId = paramMap.get("userId");
            String escalatorsId = paramMap.get("escalatorsId");

            String stationsId = paramMap.get("stationsId");
//
//			if (StringUtils.isNotEmpty(type)) {
//				userId = type;
//			}

        }
        super.channelRead(ctx, msg);
    }

    /**
     * @param userId
     * @param message
     * @Description: 将消息发送给指定在线客户端
     */
    public void sendMessage(Long userId, AlarmEntity message) {
        List<Channel> channelList = WebSocket.channelMap.get(userId);
        if (CollectionUtils.isNotEmpty(channelList)) {
            for (Channel channel : channelList) {
                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
            }
        }
    }

    public void sendMessage(Long type, String message) {
        List<Channel> channelList = WebSocket.channelMap.get(type);
        if (CollectionUtils.isNotEmpty(channelList)) {
            for (Channel channel : channelList) {
                channel.writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }

    public void sendMessage(String key, String message) {
        ConcurrentHashMap<ChannelId, Channel> channelMap = WebSocket.channelEsMap.get(key);
        if (MapUtils.isNotEmpty(channelMap)) {
            channelMap.forEach((k, v) -> {
                v.writeAndFlush(new TextWebSocketFrame(message));
            });
        }
    }

    public void sendStationMessage(String key, String message) {
        ConcurrentHashMap<ChannelId, Channel> channelMap = WebSocket.stationChannelEsMap.get(key);
        if (MapUtils.isNotEmpty(channelMap)) {
            channelMap.forEach((k, v) -> {
                v.writeAndFlush(new TextWebSocketFrame(message));
            });
        }
    }


    /**
     * @param message
     * @Description: 将消息发送给所有在线客户端
     */
    public void sendMessageAll(String message) {
        channelGroup.writeAndFlush(new TextWebSocketFrame(message));
    }

    /**
     * @param uri 传入的携带参数的路径
     * @Description 将路径参数转换成Map对象，如果路径参数出现重复参数名，将以最后的参数值为准
     */
    private static Map<String, String> getParams(String uri) {
        Map<String, String> params = new HashMap<>(10);

        int idx = uri.indexOf("?");
        if (idx != -1) {
            String[] paramsArr = uri.substring(idx + 1).split("&");

            for (String param : paramsArr) {
                idx = param.indexOf("=");
                params.put(param.substring(0, idx), param.substring(idx + 1));
            }
        }

        return params;
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            Long userId = WebSocket.userIdMap.get(ctx.channel().id());
            String escalatorsAndId = WebSocket.userIdEsMap.get(ctx.channel().id());
            if (StringUtils.isNotBlank(escalatorsAndId)) {
                if (MapUtils.isNotEmpty(WebSocket.channelEsMap.get(escalatorsAndId)) && WebSocket.channelEsMap.get(escalatorsAndId).containsKey(ctx.channel().id())) {
                    WebSocket.channelEsMap.get(escalatorsAndId).remove(ctx.channel().id());
                    WebSocket.userIdEsMap.remove(ctx.channel().id());
                }
                if (MapUtils.isNotEmpty(WebSocket.stationChannelEsMap.get(escalatorsAndId)) && WebSocket.stationChannelEsMap.get(escalatorsAndId).containsKey(ctx.channel().id())) {
                    WebSocket.stationChannelEsMap.get(escalatorsAndId).remove(ctx.channel().id());
                    WebSocket.userIdEsMap.remove(ctx.channel().id());
                }
            }

            List<Channel> channelList = WebSocket.channelMap.get(userId);
            channelList.remove(ctx.channel());
            WebSocket.userIdEsMap.remove(ctx.channel().id());
            WebSocket.channelMap.put(userId, channelList);
            // 移除ChannelGroup通道组
            boolean flag = channelGroup.remove(ctx.channel());
            if (flag) {
                onlineCount--;
                log.info("与客户端断开连接成功，当前在线人数:{}", onlineCount);
            }
            ctx.close();
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (frame instanceof TextWebSocketFrame) {
            // 无业务 回发给客户端
            log.info("接收到websocket信息:{}", ((TextWebSocketFrame) frame).text());
            ctx.channel().writeAndFlush(new TextWebSocketFrame(((TextWebSocketFrame) frame).text()));
            return;
        }
        if (frame instanceof BinaryWebSocketFrame) {
            ctx.write(frame.retain());
            return;
        }
    }
}
