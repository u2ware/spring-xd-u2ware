package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SafesystemFireServerMock extends AbstractTcpClient {

	public static void main(String[] args) throws Exception{
		
		String host = "127.0.0.1";
		try{
			host = args[0];
		}catch(Exception e){
		}

		int port = 10902;
		try{
			port = Integer.parseInt(args[1]);
		}catch(Exception e){
		}

		SafesystemFireServerMock.startup(host, port);
	}
	
	private static SafesystemFireServerMock siemensFireMaster;
	
	public static void startup(String host, int port) throws Exception{
		siemensFireMaster = new SafesystemFireServerMock();
		siemensFireMaster.setHost(host);
		siemensFireMaster.setPort(port);
		siemensFireMaster.setAutoConnection(true);
		siemensFireMaster.afterPropertiesSet();
		System.err.println("SafesystemFireServerMock Startup "+siemensFireMaster.getHost()+":"+siemensFireMaster.getPort());
	}
	public static void shutdown() throws Exception{
		siemensFireMaster.destroy();
		System.err.println("SafesystemFireServerMock Shutdown "+siemensFireMaster.getHost()+":"+siemensFireMaster.getPort());
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
    	    	String msg = "[ALM]YYYY/MM/DD HH:MM:SS ["+System.currentTimeMillis()+" "+ random.nextInt()+" ] [CR]";
    	    	return Unpooled.copiedBuffer(msg.getBytes());
    	    }
		});
	}
	
	
}
