package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpClient;
import io.github.u2ware.integration.netty.support.NettyMessagingHandler;
import io.netty.channel.ChannelPipeline;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

public class JunghoLightingHost extends AbstractTcpClient{

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
		//pipeline.addLast(new NettyLoggingHandler(getClass(), false));
		pipeline.addLast(new JunghoLightingHostHandler(getClass()));
		pipeline.addLast(new NettyMessagingHandler(getClass(), receiveChannel, sendChannel, true));
	}
}
