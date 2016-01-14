package io.github.u2ware.integration.netty.x;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;
import java.util.Map;

public class JunghoLightingMessageCodec extends MessageToMessageCodec<Map, Map>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Map msg, List<Object> out) throws Exception {

	}

	@Override
	protected void decode(ChannelHandlerContext ctx, Map msg, List<Object> out) throws Exception {

	}
}
