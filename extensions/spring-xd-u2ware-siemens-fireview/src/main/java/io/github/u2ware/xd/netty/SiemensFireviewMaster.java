package io.github.u2ware.xd.netty;

import io.github.u2ware.integration.netty.core.NettyTcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class SiemensFireviewMaster extends NettyTcpClient {

	public static void main(String[] args) throws Exception{
		
		int port = 9990;
		try{
			port = Integer.parseInt(args[0]);
		}catch(Exception e){
		}

		SiemensFireviewMaster s = new SiemensFireviewMaster();
		s.setHost("127.0.0.1");
		s.setPort(port);
		s.afterPropertiesSet();
	}
	
	
	
	private static final ByteBuf ACK = Unpooled.copiedBuffer(new byte[]{0x02, (byte)0x81, (byte)0x86, 0x03 });
	private static final ByteBuf FIRE = Unpooled.copiedBuffer(new byte[]{
			0x02, //byte stx = in.readByte();
			0x07, //short length = in.readShort();
			0x07,
			0x07, //byte tx = in.readByte();
			0x07, //byte rx = in.readByte();
			(byte)0x91, //byte op = in.readByte();
			0x07, //byte seq = in.readByte();
			(byte)'a', //String data = in.readBytes(in.readableBytes() - 1).toString(Charset.defaultCharset());
			(byte)',',
			(byte)'b',
			(byte)',',
			(byte)'c',
			(byte)',',
			(byte)'d',
			0x03 //byte etx = in.readByte();
	});
	
	
	
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		
		pipeline.addLast("idle", new IdleStateHandler(3000, 0, 0, TimeUnit.MILLISECONDS));
		pipeline.addLast("message_channel", new ChannelDuplexHandler(){

			private int seq = 0;
			
			@Override
			public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
				
				logger.debug("userEventTriggered");
				logger.debug("userEventTriggered");
				logger.debug("userEventTriggered");
				logger.debug("userEventTriggered");
				
				if(seq % 2 == 0){
					ctx.writeAndFlush(ACK.copy());
				}else{
					ctx.writeAndFlush(FIRE.copy());
				}
				
				seq++;
			}
			
    	    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	    	
    	    }
    	    
    	    @Override
    	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	    	cause.printStackTrace();
    	    }
        });
	}
}
