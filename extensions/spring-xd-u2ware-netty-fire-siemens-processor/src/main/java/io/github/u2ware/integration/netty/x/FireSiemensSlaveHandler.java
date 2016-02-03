package io.github.u2ware.integration.netty.x;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class FireSiemensSlaveHandler extends ByteToMessageDecoder{

	static final ByteBuf ETX = Unpooled.copiedBuffer(new byte[]{0x03});
	static final ByteBuf ACK = Unpooled.copiedBuffer(new byte[]{0x02, (byte)0x81, (byte)0x86, 0x03 });
	static final Map<Byte,String> OP_Codes = Maps.newHashMap();
	
	static{
		OP_Codes.put((byte)0x91, "전체 복구"); 
		OP_Codes.put((byte)0x92, "화재 발생"); 
		OP_Codes.put((byte)0x93, "화재 복구");
		OP_Codes.put((byte)0x94, "장애 발생"); 
		OP_Codes.put((byte)0x95, "장애 복구"); 
		OP_Codes.put((byte)0x96, "감시 발생"); 
		OP_Codes.put((byte)0x97, "감시 복구"); 
		OP_Codes.put((byte)0x98, "예비 경보 발생"); 
		OP_Codes.put((byte)0x99, "예비 경보 복구");		
	}
	
	protected InternalLogger logger;
	
	public FireSiemensSlaveHandler(Class<?> clazz){
		logger = InternalLoggerFactory.getInstance(clazz);
	}
	
	
	@Override
	@SuppressWarnings("unused")
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		if(in.readableBytes() == 0){
			return;
		}
		
		if(in.readableBytes() == 4){
			//logger.debug("ACK");
			in.readBytes(4);
		}else{
			
			byte stx = in.readByte();
			short length = in.readShort();
			byte tx = in.readByte();
			byte rx = in.readByte();
			byte op = in.readByte();
			byte seq = in.readByte();
			String data = in.readBytes(in.readableBytes() - 1).toString(CharsetUtil.US_ASCII);
			byte etx = in.readByte();
			
			//logger.debug("op: "+op+" "+OP_Codes.get(op));
			//logger.debug("data : "+data);
			/*
			String[] args = StringUtils.commaDelimitedListToStringArray(data);
			logger.debug("Date&Time : "+args[0]);
			logger.debug("Address : "+args[1]);
			logger.debug("Area Name : "+args[2]);
			logger.debug("User Message : "+args[3]);
			*/
			out.add(new FireSiemensResponse(OP_Codes.get(op)+" "+data));
		}
		
		ctx.writeAndFlush(ACK.copy());
	}
}
