package com.nick.api.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface WebSocket {
	
    // 维护userId和channel关系
	public static ConcurrentHashMap<Long, List<Channel>> channelMap = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, ConcurrentHashMap<ChannelId,Channel>> channelEsMap = new ConcurrentHashMap<>();

	//站台级通道
	public static ConcurrentHashMap<String, ConcurrentHashMap<ChannelId,Channel>> stationChannelEsMap = new ConcurrentHashMap<>();
	
	// 维护channelId和userId关系
	public static ConcurrentHashMap<ChannelId, Long> userIdMap = new ConcurrentHashMap<>();


	public static ConcurrentHashMap<ChannelId, Long> typeMap = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<ChannelId, String>userIdEsMap = new ConcurrentHashMap<>();

}
