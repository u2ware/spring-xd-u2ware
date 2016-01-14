package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpClient;
import io.github.u2ware.integration.netty.support.NettyLoggingHandler;
import io.github.u2ware.integration.netty.support.NettyMessagingHandler;
import io.netty.channel.ChannelPipeline;

import java.util.List;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import com.google.common.collect.Lists;

public class JunghoLightingHost extends AbstractTcpClient{

	private MessageChannel sendChannel;
	private PollableChannel receiveChannel;
	private List<Object> dataSet = Lists.newArrayList();
	
	public void setSendChannel(MessageChannel sendChannel) {
		this.sendChannel = sendChannel;
	}
	public void setReceiveChannel(PollableChannel receiveChannel) {
		this.receiveChannel = receiveChannel;
	}
	
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {

		pipeline.addLast("logging", new NettyLoggingHandler(getClass()));
		pipeline.addLast("messageDecode", new JunghoLightingHostHandler(getClass(), dataSet));
		pipeline.addLast("messageHandle", new NettyMessagingHandler(getClass(), receiveChannel, sendChannel, dataSet));
	}
}
