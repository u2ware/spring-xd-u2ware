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

public class HyundaiElevatorMaster extends AbstractTcpClient{
	
	private MessageChannel sendChannel;
	private PollableChannel receiveChannel;
	private int idleTimeout;

	public void setSendChannel(MessageChannel sendChannel) {
		this.sendChannel = sendChannel;
	}
	public void setReceiveChannel(PollableChannel receiveChannel) {
		this.receiveChannel = receiveChannel;
	}
	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {
		pipeline.addLast(new NettyLoggingHandler(getClass()));
		if(idleTimeout > 0){
			pipeline.addLast(new IdleStateHandler(idleTimeout, 0, 0, TimeUnit.MILLISECONDS));
		}
		pipeline.addLast(new NettyLoggingHandler(getClass()));
		pipeline.addLast(new DelimiterBasedFrameDecoder(2048, false, HyundaiElevatorMasterHandler.ETX));
		pipeline.addLast(new HyundaiElevatorMasterHandler(getClass()));
		pipeline.addLast(new NettyMessagingHandler(getClass(), receiveChannel, sendChannel));
	}		
}
