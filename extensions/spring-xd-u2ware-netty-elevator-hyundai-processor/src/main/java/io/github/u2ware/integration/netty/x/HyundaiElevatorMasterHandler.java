package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.support.NettyHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class HyundaiElevatorMasterHandler extends MessageToMessageCodec<ByteBuf, HyundaiElevatorRequest>{

	static final ByteBuf ETX = Unpooled.wrappedBuffer("ETX".getBytes());
	static final Map<String,String> 운행s = Maps.newHashMap();
	static final Map<String,String> 방향s = Maps.newHashMap();
	static final Map<String,String> 도어s = Maps.newHashMap();
	
	static {
		운행s.put("00", "승강기-감시반 통신이상");
		운행s.put("01", "자동운전 (정상운행)");
		운행s.put("02", "수동운전 (보수중)");
		운행s.put("03","독립운전 (이사중)");
		운행s.put("04","소방운전 (관제운전)");
		운행s.put("05","승강기 고장");
		운행s.put("06","파킹상태");
		운행s.put("07","만원");
		운행s.put("08","비감시");
		운행s.put("09","전원차단");

		방향s.put("0", "방향없음");
		방향s.put("1", "UP 방향");
		방향s.put("2", "DOWN 방향");
	
		도어s.put("0", "Door Closed");
		도어s.put("2", "Door Opend");
	}

	protected final InternalLogger logger ;
	
	public HyundaiElevatorMasterHandler(Class<?> clazz){
		this.logger = InternalLoggerFactory.getInstance(clazz);
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		//logger.debug("userEventTriggered ");
		ctx.channel().writeAndFlush(new HyundaiElevatorRequest());
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, HyundaiElevatorRequest msg, List<Object> out) throws Exception {
		//logger.debug("encode: "+msg.getClass());
		ByteBuf buf = Unpooled.wrappedBuffer("STXRETX".getBytes());
		out.add(buf);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//logger.debug("decode: "+in.getClass());
		if(in.readableBytes() == 0){
			return;
		}

		List<HyundaiElevatorResponse> result = Lists.newArrayList(); 
		
		@SuppressWarnings("unused")
		String stx = in.readBytes(3).toString(Charset.defaultCharset());
		//logger.debug(stx+" "+in.readableBytes());

		while(in.readableBytes() > 3){
			String 동 = in.readBytes(4).toString(Charset.defaultCharset());
			String 호기 = in.readBytes(2).toString(Charset.defaultCharset());
			String 운행 = in.readBytes(2).toString(Charset.defaultCharset());
			String 층수 = in.readBytes(2).toString(Charset.defaultCharset());
			String 방향 = in.readBytes(1).toString(Charset.defaultCharset());
			String 도어 = in.readBytes(1).toString(Charset.defaultCharset());
			String 정지예정층 = in.readBytes(2).toString(Charset.defaultCharset());
			in.readBytes(16);//String 카호출 = in.readBytes(16).toString(Charset.defaultCharset());
			in.readBytes(16);//String 상향호출 = in.readBytes(16).toString(Charset.defaultCharset());
			in.readBytes(16);//String 하향호출 = in.readBytes(16).toString(Charset.defaultCharset());
			
			
			Map<String, Object> headers = Maps.newHashMap();
			headers.put(NettyHeaders.REMOTE_ADDRESS, ctx.channel().remoteAddress().toString());

			result.add(new HyundaiElevatorResponse(동+"_"+호기+"_3", 운행, "운행", 운행s.get(운행)));
			result.add(new HyundaiElevatorResponse(동+"_"+호기+"_4", 층수, "층수", null         ));
			result.add(new HyundaiElevatorResponse(동+"_"+호기+"_5", 방향, "방향", 방향s.get(방향)));
			result.add(new HyundaiElevatorResponse(동+"_"+호기+"_6", 도어, "도어", 도어s.get(도어)));
			result.add(new HyundaiElevatorResponse(동+"_"+호기+"_7", 정지예정층, "정지예정층", null ));
			
		}

		out.add(result);
		
		@SuppressWarnings("unused")
		String etx = in.readBytes(3).toString(Charset.defaultCharset());
		//logger.debug(etx+" "+in.readableBytes());
	}	
}
