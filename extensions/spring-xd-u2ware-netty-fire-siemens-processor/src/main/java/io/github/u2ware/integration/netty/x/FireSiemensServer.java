package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpServer;
import io.github.u2ware.integration.netty.support.NettyLoggingHandler;
import io.github.u2ware.integration.netty.support.NettyMessagingHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

public class FireSiemensServer extends AbstractTcpServer{

	private MessageChannel sendChannel;
	private PollableChannel receiveChannel;
	private boolean messagingPreservation;
	
	public void setSendChannel(MessageChannel sendChannel) {
		this.sendChannel = sendChannel;
	}
	public void setReceiveChannel(PollableChannel receiveChannel) {
		this.receiveChannel = receiveChannel;
	}
	public void setMessagingPreservation(boolean messagingPreservation) {
		this.messagingPreservation = messagingPreservation;
	}
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {

		pipeline.addLast(new NettyLoggingHandler(getClass(), false));
		pipeline.addLast(new DelimiterBasedFrameDecoder(2048, false, FireSiemensSlaveHandler.ETX));
		pipeline.addLast(new FireSiemensSlaveHandler(getClass()));
		pipeline.addLast(new NettyMessagingHandler(
				getClass(), 
				(messagingPreservation ? receiveChannel : null), 
				sendChannel, 
				messagingPreservation)
		);
	}
}
