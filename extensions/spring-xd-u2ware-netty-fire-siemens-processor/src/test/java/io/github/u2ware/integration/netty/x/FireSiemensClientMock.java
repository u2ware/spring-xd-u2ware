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

public class FireSiemensClientMock extends AbstractTcpClient {

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

		FireSiemensClientMock.startup(host, port);
	}
	
	private static FireSiemensClientMock siemensFireMaster;
	
	public static void startup(String host, int port) throws Exception{
		siemensFireMaster = new FireSiemensClientMock();
		siemensFireMaster.setHost(host);
		siemensFireMaster.setPort(port);
		siemensFireMaster.setAutoConnection(true);
		siemensFireMaster.afterPropertiesSet();
		System.err.println("SiemensFireMaster Startup "+siemensFireMaster.getHost()+":"+siemensFireMaster.getPort());
	}
	public static void shutdown() throws Exception{
		siemensFireMaster.destroy();
		System.err.println("SiemensFireMaster Shutdown "+siemensFireMaster.getHost()+":"+siemensFireMaster.getPort());
	}
	
	
	
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		
		//pipeline.addLast("msg", new NettyLoggingHandler(getClass()));
		pipeline.addLast("idle", new IdleStateHandler(3000, 0, 0, TimeUnit.MILLISECONDS));
		pipeline.addLast("message_channel", new ChannelDuplexHandler(){

			private int seq = 0;
			private Random random = new Random();
			
			@Override
			public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
				
    			if(seq % 2 == 0){
					ctx.writeAndFlush(ACK());
				}else{
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
    	    
    	    
    	    public ByteBuf ACK(){
    	    	return Unpooled.copiedBuffer(new byte[]{0x02, (byte)0x81, (byte)0x86, 0x03 });
    	    }
    	    public ByteBuf FIRE(){
    	    	
    	    	return Unpooled.copiedBuffer(new byte[]{
    	    			0x02, //byte stx = in.readByte();
    	    			0x07, //short length = in.readShort();
    	    			0x07,
    	    			0x07, //byte tx = in.readByte();
    	    			0x07, //byte rx = in.readByte();
    	    			(byte)0x91, //byte op = in.readByte();
    	    			0x07, //byte seq = in.readByte();
    	    			(byte)random.nextInt(), //String data = in.readBytes(in.readableBytes() - 1).toString(Charset.defaultCharset());
    	    			(byte)',',
    	    			(byte)random.nextInt(),
    	    			(byte)',',
    	    			(byte)random.nextInt(),
    	    			(byte)',',
    	    			(byte)random.nextInt(),
    	    			0x03 //byte etx = in.readByte();
    	    	});
    	    }
		});
	}
	
	
}
