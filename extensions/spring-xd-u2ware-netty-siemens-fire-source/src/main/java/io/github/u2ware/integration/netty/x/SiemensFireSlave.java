package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpServer;
import io.github.u2ware.integration.netty.support.NettyLoggingHandler;
import io.github.u2ware.integration.netty.support.NettyMessagingHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

public class SiemensFireSlave extends AbstractTcpServer{

	private MessageChannel sendChannel;
	private PollableChannel receiveChannel;
	
	public void setSendChannel(MessageChannel sendChannel) {
		this.sendChannel = sendChannel;
	}
	public void setReceiveChannel(PollableChannel receiveChannel) {
		this.receiveChannel = receiveChannel;
	}
	
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {

		pipeline.addLast(new NettyLoggingHandler(getClass(), false));
		pipeline.addLast(new DelimiterBasedFrameDecoder(2048, false, SiemensFireSlaveHandler.ETX));
		pipeline.addLast(new SiemensFireSlaveHandler(getClass()));
		pipeline.addLast(new NettyMessagingHandler(getClass(), receiveChannel, sendChannel, true));
	}
	
}
