package io.github.u2ware.xd.netty;

import io.github.u2ware.integration.netty.core.NettyTcpServer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class HyundaiElevatorSlave extends NettyTcpServer{

	public static void main(String[] args) throws Exception{
		
		int port = 9990;
		try{
			port = Integer.parseInt(args[0]);
		}catch(Exception e){
		}

		HyundaiElevatorSlave s = new HyundaiElevatorSlave();
		s.setPort(port);
		s.afterPropertiesSet();
	}

	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		pipeline.addLast(new DelimiterBasedFrameDecoder(2048, false, Unpooled.wrappedBuffer("ETX".getBytes())));
        pipeline.addLast(new ChannelInboundHandlerAdapter(){
    		@Override
    		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    			String res = "STX444422"+randomRange(10000000, 99999999)+"000000000000000000000000000000000000000000000000ETX";
    			ctx.writeAndFlush(Unpooled.wrappedBuffer(res.getBytes()));
    		}
    		
    		public int randomRange(int n1, int n2) {
    		    return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    		}    		
        });
	}
}
