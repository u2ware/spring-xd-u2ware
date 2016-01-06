package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class SiemensFireMaster extends AbstractTcpClient {

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

		SiemensFireMaster.startup(host, port);
	}
	
	private static SiemensFireMaster siemensFireMaster;
	
	public static void startup(String host, int port) throws Exception{
		siemensFireMaster = new SiemensFireMaster();
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
				
				if(seq % 2 == 0){
					ctx.writeAndFlush(ACK.copy());
				}else{
	    			System.err.println("SiemensFireMaster ...");
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
