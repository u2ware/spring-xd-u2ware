package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpClient;
import io.github.u2ware.integration.netty.support.NettyLoggingHandler;
import io.github.u2ware.integration.netty.support.NettyMessagingHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

public class HpnrtElevatorClient extends AbstractTcpClient{
	
	private MessageChannel sendChannel;
	private PollableChannel receiveChannel;
	private boolean messageKeep = false;

	public void setSendChannel(MessageChannel sendChannel) {
		this.sendChannel = sendChannel;
	}
	public void setReceiveChannel(PollableChannel receiveChannel) {
		this.receiveChannel = receiveChannel;
	}
	public void setMessageKeep(boolean messageKeep) {
		this.messageKeep = messageKeep;
	}
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		pipeline.addLast(new NettyLoggingHandler(getClass()));
		pipeline.addLast(new FixedLengthFrameDecoder( 1 + 2 + (4*8) + 1 + 1  ) );
		pipeline.addLast(new HpnrtElevatorClientHandler(getClass()));		
		pipeline.addLast(new NettyMessagingHandler(getClass(), receiveChannel, sendChannel, messageKeep));
	}
}
