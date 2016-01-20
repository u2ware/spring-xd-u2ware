package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpClient;
import io.github.u2ware.integration.netty.support.NettyLoggingHandler;
import io.github.u2ware.integration.netty.support.NettyMessagingHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

public class ElevatorHyundaiClient extends AbstractTcpClient{
	
	private MessageChannel sendChannel;
	private PollableChannel receiveChannel;
	
	private int messagingTimeout;
	private boolean messagingPreservation = false;

	public void setSendChannel(MessageChannel sendChannel) {
		this.sendChannel = sendChannel;
	}
	public void setReceiveChannel(PollableChannel receiveChannel) {
		this.receiveChannel = receiveChannel;
	}
	public void setMessagingTimeout(int messagingTimeout) {
		this.messagingTimeout = messagingTimeout;
	}
//	public void setMessagingPreservation(boolean messagingPreservation) {
//		this.messagingPreservation = messagingPreservation;
//	}
	
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		pipeline.addLast(new NettyLoggingHandler(getClass(), false));
		if(messagingTimeout > 1000){
			pipeline.addLast(new IdleStateHandler(messagingTimeout, 0, 0, TimeUnit.MILLISECONDS));
		}
		pipeline.addLast(new DelimiterBasedFrameDecoder(2048, false, ElevatorHyundaiClientHandler.ETX));
		pipeline.addLast(new ElevatorHyundaiClientHandler(getClass()));
		pipeline.addLast(new NettyMessagingHandler(getClass(), receiveChannel, sendChannel, messagingPreservation));
	}
}