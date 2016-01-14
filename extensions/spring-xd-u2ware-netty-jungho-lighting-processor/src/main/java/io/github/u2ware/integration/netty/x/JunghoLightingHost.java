package io.github.u2ware.integration.netty.x;

import io.github.u2ware.integration.netty.core.AbstractTcpClient;
import io.github.u2ware.integration.netty.support.NettyMessagingHandler;
import io.netty.channel.ChannelPipeline;

import org.springframework.messaging.MessageChannel;

public class JunghoLightingHost extends AbstractTcpClient{

	private MessageChannel sendChannel;

	public void setSendChannel(MessageChannel sendChannel) {
		this.sendChannel = sendChannel;
	}
	
	@Override
	protected void initChannelPipeline(ChannelPipeline pipeline) throws Exception {

		//pipeline.addLast("logging", new NettyLoggingHandler(getClass()));
		pipeline.addLast("messageDecode", new JunghoLightingDecoder());
		pipeline.addLast("messageHandle", new NettyMessagingHandler(sendChannel, null, 1000));
	}
}
