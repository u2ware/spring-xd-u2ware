package io.github.u2ware.integration.netty.x;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Maps;

public class JunghoLightingHostHandler extends ByteToMessageDecoder{

	
	//private byte STX = (byte)0x02;
	private byte ETX = (byte)0x03;
	
	private byte ACK = (byte)0x06;
	private byte NAK = (byte)0x15;
	private byte EOT = (byte)0x04;

	private AtomicInteger msgNumber = new AtomicInteger(0);
	private AtomicInteger lcuNumber = new AtomicInteger(0);
	private NumberFormat msgFormat = new DecimalFormat("000");
	private NumberFormat lcuFormat = new DecimalFormat("00");
	private AtomicInteger pollingCount = new AtomicInteger(0);
	
	private ByteBuf frame;

	private final InternalLogger logger;
	private final Map<String, JunghoLightingResponse> dataSet;
	
	public JunghoLightingHostHandler(Class<?> clazz){
		this.logger = InternalLoggerFactory.getInstance(clazz);
		this.dataSet = Maps.newHashMap();
	}
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush( POLLING() );
		logger.debug("# SEND POLLING "+pollingCount.get()+" #");
	}

	protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		if(in.readableBytes() == 1){
			byte res = in.readByte();
			
			if(res == ACK){
				logger.debug("# RECEIVED ACK #");
				
				if(pollingCount.get() == 0){
					pollingCount.set(pollingCount.get()+1);

					ctx.writeAndFlush( TEXT() );
					//logger.debug("# SEND TEXT #");

				}else{
					ctx.writeAndFlush( EOT() );
					logger.debug("# SEND EOT  #");
					Thread.sleep(1000);
					
					pollingCount.set(0);
					ctx.writeAndFlush( POLLING() );
					logger.debug("# SEND POLLING "+pollingCount.get()+" #");
				}
				
			}else if(res == NAK){
				logger.debug("# RECEIVED NAK #");
				
				ctx.writeAndFlush( EOT() );
				logger.debug("# SEND EOT #");
				Thread.sleep(1000);

				pollingCount.set(0);
				ctx.writeAndFlush( POLLING() );
				logger.debug("# SEND POLLING "+pollingCount.get()+" #");

			}else if(res == EOT){
				pollingCount.set(pollingCount.get()+1);
				logger.debug("# RECEIVED EOT #");

				Thread.sleep(1000);

				if(pollingCount.get() > 3){
					pollingCount.set(0);
					ctx.writeAndFlush( SELECTING() );
					logger.debug("# SEND SELECTING #");

				}else{
					ctx.writeAndFlush( POLLING() );
					logger.debug("# SEND POLLING "+pollingCount.get()+" #");
				}
			}

		}else{
			
			byte[] d = new byte[in.readableBytes()];
			in.readBytes(d);

			if(frame == null){
				frame = Unpooled.buffer();
			}
			frame = frame.writeBytes(d);
			
			
            if(d[d.length - 2] == ETX){
            	
				TEXT(frame, out);
				frame = null;
				//logger.debug("# RECEIVED TEXT #");
            	
				ctx.writeAndFlush( ACK() );
				logger.debug("# SEND ACK     #");


				pollingCount.set(0);
            }
		}
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	logger.debug("exceptionCaught: ",cause);
    }
    
	private ByteBuf ACK(){
		return Unpooled.wrappedBuffer(new byte[]{ACK});
	}
	private ByteBuf EOT(){
		return Unpooled.wrappedBuffer(new byte[]{EOT});
	}
	
	private ByteBuf POLLING(){
		return Unpooled.wrappedBuffer(new byte[]{(byte)0x30, (byte)0x40, (byte)0x05 });
	}
	private ByteBuf SELECTING(){
		return Unpooled.wrappedBuffer(new byte[]{(byte)0x30, (byte)0x20, (byte)0x05 });
	}

	private ByteBuf TEXT(){

		msgNumber.set(msgNumber.get()+1);
		lcuNumber.set(lcuNumber.get()+1);
		if(msgNumber.get() > 999){msgNumber.set(1);}
		if(lcuNumber.get() > 3) {lcuNumber.set(1);}
		
		String msg = msgFormat.format(msgNumber.get());
		String cmd = "$d1";
		String lcu = lcuFormat.format(lcuNumber.get());
		ByteBuf buf = Unpooled.buffer(14);
		buf.writeByte((byte)0x02); //STX
		buf.writeByte((byte)0x30); //SA
		buf.writeByte((byte)0x20); //UA
		buf.writeByte((byte)0x0F); //SI
		buf.writeBytes(msg.getBytes(), 0 , 3); //MSG
		buf.writeBytes(cmd.getBytes(), 0 , 3); //CMD
		buf.writeBytes(lcu.getBytes()); //LCU
		buf.writeByte((byte)0x03); //ETX
		byte bcc = 0;
		for(int i = 1 ; i < 13; i++){
			bcc = (byte)(bcc ^ buf.getByte(i));
		}
		buf.writeByte(bcc); //BCC
		
		logger.info("# SEND TEXT (msgNumber:"+msgNumber.get()+",  lcuNumber:"+lcuNumber.get()+")");
		return buf;
	}

	@SuppressWarnings("unused")
	private void TEXT(ByteBuf res, List<Object> out){

		logger.info(res.toString(CharsetUtil.UTF_8));
		
		int length = res.readableBytes();
		byte stx = res.readByte(); //System.out.println("ETX :"+Integer.toHexString(stx));
		byte sa = res.readByte();//System.out.println("SA :"+Integer.toHexString(stx));
		byte ua = res.readByte();//System.out.println("UA :"+Integer.toHexString(stx));
		byte si = res.readByte();//System.out.println("SI :"+Integer.toHexString(stx));
		byte[] msg = new byte[3]; res.readBytes(msg);//System.out.println("MSG :"+new String(msg));
		byte[] cmd = new byte[3]; res.readBytes(cmd);//System.out.println("CMD :"+new String(cmd));
		byte[] txt_lcu = new byte[2]; res.readBytes(txt_lcu);//System.out.println("txt_lcu :"+new String(txt_lcu));
		byte[] txt_status= new byte[1]; res.readBytes(txt_status);//System.out.println("txt_status :"+new String(txt_lcu));
		byte[] txt_body = new byte[res.readableBytes() - 9];res.readBytes(txt_body);//System.out.println("TXT :"+new String(txt_body));
		byte etx = res.readByte();//System.out.println("ETX :"+Integer.toHexString(stx));
		byte bcc = res.readByte();//System.out.println("BCC :"+Integer.toHexString(stx));

		logger.info("# RECEIVED TEXT (msgNumber:"+msgNumber.get()+",  lcuNumber:"+lcuNumber.get()+")");
		
		String id = null;
		String state = null;
		Object value = null;
		
		int lcu = Integer.parseInt(new String(txt_lcu));
		int sw  = 0;
		int no  = 0;

		for(int i = 0; i < txt_body.length; i++) {

			String binary = toBinaryString(txt_body[i]);
			sw = i/2;

			no++;
			state = binary.substring(2 , 5);

			value = toValueString(state);
			id = lcu+"_"+sw+"_"+no;
			dataSet.put(id,  new JunghoLightingResponse(id, value));
			//logger.debug(id+"="+value);
			
			no++;
			state = binary.substring(5 , 8);

			value = toValueString(state);
			id = lcu+"_"+sw+"_"+no;
			dataSet.put(id,  new JunghoLightingResponse(id, value));

			if(no == 4) no = 0;
		}
		
		out.add(dataSet.values());
	}

	private String toBinaryString(byte b){
		String binary = Integer.toBinaryString(b);
		StringBuilder result = new StringBuilder();
		for(int i = 0 ; i < 8-binary.length(); i++){
			result.append("0");
		}
		result.append(binary);
		return result.toString();
	}
	private Object toValueString(String state){
		if("001".equals(state)){
			return 0;//"OFF";
		}else if("010".equals(state)){
			return 1;//"ON";
		}else if("011".equals(state)){
			return "delay";
		}else if("100".equals(state)){
			return "not exist";
		}else if("101".equals(state)){
			return "error";
		}else{
			return "unknown";
		}
	}
	
}
