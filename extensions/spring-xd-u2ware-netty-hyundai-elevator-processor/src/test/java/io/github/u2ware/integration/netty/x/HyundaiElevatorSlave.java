package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpServer;
import io.github.u2ware.integration.netty.support.NettyLoggingHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class HyundaiElevatorSlave extends AbstractTcpServer{

	public static void main(String[] args) throws Exception{
		
		int port = 10901;
		try{
			port = Integer.parseInt(args[0]);
		}catch(Exception e){
		}

		HyundaiElevatorSlave.startup(port);
	}
	
	private static HyundaiElevatorSlave hyundaiElevatorSlave;
	
	public static void startup(int port) throws Exception{
		hyundaiElevatorSlave = new HyundaiElevatorSlave();
		hyundaiElevatorSlave.setPort(port);
		hyundaiElevatorSlave.afterPropertiesSet();
		System.err.println("HyundaiElevatorSlave Startup "+hyundaiElevatorSlave.getPort());
	}
	public static void shutdown() throws Exception{
		hyundaiElevatorSlave.destroy();
		System.err.println("HyundaiElevatorSlave Shutdown "+hyundaiElevatorSlave.getPort());
	}
	

	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		pipeline.addLast(new NettyLoggingHandler(getClass()));
		pipeline.addLast(new DelimiterBasedFrameDecoder(2048, false, Unpooled.wrappedBuffer("ETX".getBytes())));
        pipeline.addLast(new ChannelDuplexHandler(){
    		@Override
    		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    			System.err.println("HyundaiElevatorSlave ...");
    			String res = "STX444422"+randomRange(10000000, 99999999)+"000000000000000000000000000000000000000000000000ETX";
    			ctx.writeAndFlush(Unpooled.wrappedBuffer(res.getBytes()));
    		}
    		
    		public int randomRange(int n1, int n2) {
    		    return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    		}    		
        });
	}
}
