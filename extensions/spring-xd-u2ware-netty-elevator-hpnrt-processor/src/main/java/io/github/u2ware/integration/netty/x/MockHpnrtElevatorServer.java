package io.github.u2ware.integration.netty.x;

import java.util.concurrent.TimeUnit;

import io.github.u2ware.integration.netty.core.AbstractTcpServer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class MockHpnrtElevatorServer extends AbstractTcpServer{

	public static void main(String[] args) throws Exception{
		
		int port = 5001;
		try{
			port = Integer.parseInt(args[0]);
		}catch(Exception e){
		}

		MockHpnrtElevatorServer.startup(port);
	}
	
	private static MockHpnrtElevatorServer hyundaiElevatorSlave;
	
	public static void startup(int port) throws Exception{
		hyundaiElevatorSlave = new MockHpnrtElevatorServer();
		hyundaiElevatorSlave.setPort(port);
		hyundaiElevatorSlave.afterPropertiesSet();
	}
	public static void shutdown() throws Exception{
		hyundaiElevatorSlave.destroy();
	}
	

	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		
		pipeline.addLast("idle", new IdleStateHandler(3000, 0, 0, TimeUnit.MILLISECONDS));
		pipeline.addLast("message_channel", new ChannelDuplexHandler(){
			
			@Override
			public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

				//ByteBuf buf = Unpooled.
				
				
			}
		});
	}
}
/*
+-------------------------------------------------+
|  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 02 23 00 01 02 00 02 32 00 00 00 02 02 00 01 31 |.#.....2.......1|
|00000010| 00 00 00 03 02 00 01 31 00 00 00 04 02 00 05 35 |.......1.......5|
|00000020| 00 00 00 25 03                                  |...%.           |
+--------+-------------------------------------------------+----------------+
*/