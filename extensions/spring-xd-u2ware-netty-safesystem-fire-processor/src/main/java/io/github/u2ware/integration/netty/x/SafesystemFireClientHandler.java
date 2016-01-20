package io.github.u2ware.integration.netty.x;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;

public class SafesystemFireClientHandler extends ByteToMessageDecoder{

	//static final ByteBuf ETX = Unpooled.copiedBuffer(new byte[]{0x03});
	
	protected InternalLogger logger;
	
	public SafesystemFireClientHandler(Class<?> clazz){
		logger = InternalLoggerFactory.getInstance(clazz);
	}
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		String message = in.readBytes(in.readableBytes()).toString(CharsetUtil.UTF_8);
		out.add(new SafesystemFireResponse(message));
	}
}
