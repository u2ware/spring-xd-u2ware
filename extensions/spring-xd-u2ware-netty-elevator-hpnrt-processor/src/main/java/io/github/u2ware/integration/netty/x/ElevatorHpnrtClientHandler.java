package io.github.u2ware.integration.netty.x;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;

import com.google.common.collect.Lists;

public class ElevatorHpnrtClientHandler extends ByteToMessageDecoder{

	static final ByteBuf ETX = Unpooled.wrappedBuffer(new byte[]{0x03});
	
	protected InternalLogger logger;
	
	public ElevatorHpnrtClientHandler(Class<?> clazz){
		logger = InternalLoggerFactory.getInstance(clazz);
	}
	
	@SuppressWarnings("unused")
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		List<ElevatorHpnrtResponse> result = Lists.newArrayList();
		
		//logger.debug("decode: "+in.readableBytes());
		
		byte STX = in.readByte();//System.out.println(STX);
		short LENGTH = in.readShort();//System.out.println(LENGTH);

		while(in.readableBytes() > 2){
			
			byte 호기번호 = in.readByte();//System.out.println("호기번호 : "+호기번호);
			byte 운행모드 = in.readByte();//System.out.println("운행모드 : "+운행모드);
			byte 운행방향 = in.readByte();//System.out.println("운행방향 : "+운행방향);
			byte 운행층 = in.readByte();//System.out.println("운행층 : "+운행층);
			String 운행층문자 = in.readBytes(3).toString(CharsetUtil.US_ASCII);//System.out.println("운행층문자 : "+운행층문자);
			byte 문열림 = in.readByte();//System.out.println("문열림 : "+문열림);
			
			//logger.debug(호기번호+"/ "+운행모드+" / "+운행방향+" / "+운행층+" : "+운행층문자+ " "+문열림);
			
			result.add(new ElevatorHpnrtResponse(호기번호+"_mode",  운행모드,  호기번호+"호기 운행모드",   운행모드(운행모드), 0l));
			result.add(new ElevatorHpnrtResponse(호기번호+"_axis",  운행방향,  호기번호+"호기 운행방향",   운행방향(운행방향), null));
			result.add(new ElevatorHpnrtResponse(호기번호+"_floor", 운행층문자, 호기번호+"호기 운행층문자", 운행층문자, null));
			result.add(new ElevatorHpnrtResponse(호기번호+"_door",  문열림,    호기번호+"호기 문열림",    문열림(문열림), null));
		}

		byte CHECKSUM = in.readByte();//System.out.println(CHECKSUM);
		byte ETX = in.readByte();//System.out.println(ETX);
		
		out.add(result);
	}
	
	
	private String 운행모드(byte v){
		switch(v){
			case (byte)0x00 : return "기타";
			case (byte)0x01 : return "운행정지";
			case (byte)0x02 : return "자동운전";
			case (byte)0x03 : return "전용운전";
			case (byte)0x04 : return "수동운전";
			case (byte)0x05 : return "비상운전";
			case (byte)0x06 : return "고장";
			case (byte)0x07 : return "파킹운전";
			case (byte)0x08 : return "보수운전";
			case (byte)0x09 : return "구출운전";
			case (byte)0x0A : return "귀착운전";
			case (byte)0x0B : return "백업운전";
			case (byte)0x0C : return "자가발관제";
			case (byte)0x0D : return "지진관제";
			case (byte)0x0E : return "소방운전";
			case (byte)0x0F : return "화재관제";
			default : return null;
		}
	}
	private String 운행방향(byte v){
		switch(v){
			case (byte)0x00 : return "STOP";
			case (byte)0x01 : return "UP";
			case (byte)0x02 : return "DOWN";
			default : return null;
		}
	}
	private String 문열림(byte v){
		switch(v){
			case (byte)0x00 : return "CLOSE";
			case (byte)0x01 : return "OPEN";
			default : return null;
		}
	}
}
