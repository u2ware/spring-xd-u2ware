package io.github.u2ware.integration.netty.x;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

public class FireSafesystemClientHandler extends ByteToMessageDecoder{

	static final ByteBuf CR = Unpooled.copiedBuffer(new byte[]{0x0A, 0x0A});
	
	protected InternalLogger logger;
	
	public FireSafesystemClientHandler(Class<?> clazz){
		logger = InternalLoggerFactory.getInstance(clazz);
	}
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		String message = in.readBytes(in.readableBytes()).toString(Charset.forName("euc-kr"));
		out.add(new FireSafesystemResponse(message));
	}
}
