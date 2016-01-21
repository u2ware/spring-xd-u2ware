package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FireSafesystemServerMock extends AbstractTcpServer {

	public static void main(String[] args) throws Exception{
		
		int port = 12000;
		try{
			port = Integer.parseInt(args[0]);
		}catch(Exception e){
		}

		FireSafesystemServerMock.startup(port);
	}
	
	private static FireSafesystemServerMock mockServer;
	
	public static void startup(int port) throws Exception{
		mockServer = new FireSafesystemServerMock();
		mockServer.setPort(port);
		mockServer.afterPropertiesSet();
		System.err.println("FireSafesystemServerMock Startup <localhost>:"+mockServer.getPort());
	}
	public static void shutdown() throws Exception{
		mockServer.destroy();
		System.err.println("FireSafesystemServerMock Shutdown <localhost>:"+mockServer.getPort());
	}
	
	
	
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		
		pipeline.addLast("idle", new IdleStateHandler(3000, 0, 0, TimeUnit.MILLISECONDS));
		pipeline.addLast("message_channel", new ChannelDuplexHandler(){

			private int seq = 0;
			private Random random = new Random();
			
			@Override
			public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
				
				if(seq % 2 == 0){
					ctx.writeAndFlush(FIRE());
				}
				seq++;
			}
			
    	    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	    	
    	    }
    	    
    	    @Override
    	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	    	cause.printStackTrace();
    	    }
    	    
    	    
    	    public ByteBuf FIRE(){
    	    	String msg = "[ALM]YYYY/MM/DD HH:MM:SS ["+System.currentTimeMillis()+" "+ random.nextInt()+"]";
    	    	
    	    	ByteBuf buf = Unpooled.buffer();
    	    	buf.writeBytes(msg.getBytes());
    	    	buf.writeByte((byte)0x0D);
    	    	
    	    	return buf;
    	    }
		});
	}
	
	
}
