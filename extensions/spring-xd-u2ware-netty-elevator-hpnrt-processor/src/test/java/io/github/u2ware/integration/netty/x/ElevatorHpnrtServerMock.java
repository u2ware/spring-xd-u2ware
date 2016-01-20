package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import org.springframework.util.StringUtils;

public class ElevatorHpnrtServerMock extends AbstractTcpServer{

	public static void main(String[] args) throws Exception{
		
		int port = 15001;
		try{
			port = Integer.parseInt(args[0]);
		}catch(Exception e){
		}

		ElevatorHpnrtServerMock.startup(port);
	}
	
	private static ElevatorHpnrtServerMock mockServer;
	
	public static void startup(int port) throws Exception{
		mockServer = new ElevatorHpnrtServerMock();
		mockServer.setPort(port);
		mockServer.afterPropertiesSet();
	}
	public static void shutdown() throws Exception{
		mockServer.destroy();
	}
	

	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		
		pipeline.addLast("idle", new IdleStateHandler(3000, 0, 0, TimeUnit.MILLISECONDS));
		pipeline.addLast("message_channel", new ChannelDuplexHandler(){
			public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
				ctx.writeAndFlush(RESPONSE());
			}
		});
	}
	
	public ByteBuf RESPONSE(){
		
		String src = "02 23 00 01 02 00 02 32 00 00 00 02 02 00 01 31 "
					+"00 00 00 03 02 00 01 31 00 00 00 04 02 00 05 35 "
					+"00 00 00 25 03";
		
		String[] hex = StringUtils.delimitedListToStringArray(src, " ");
		
		ByteBuf buf = Unpooled.buffer(hex.length);
		for(int i=0 ; i < hex.length; i++){
			
			int value = Integer.parseInt(hex[i], 16);
			buf.writeByte(value);
		}
		return buf;
	}	
}