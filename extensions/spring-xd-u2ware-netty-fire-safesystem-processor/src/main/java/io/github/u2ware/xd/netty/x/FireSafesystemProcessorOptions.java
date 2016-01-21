package io.github.u2ware.xd.netty.x;

import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ProfileNamesProvider;

public class FireSafesystemProcessorOptions implements ProfileNamesProvider{

	private static final String use_json_input = "use_json_input";
	private static final String dont_use_json_input = "dont_use_json_input";
	
	private static final String use_json_output = "use_json_output";
	private static final String dont_use_json_output = "dont_use_json_output";

	private String host;
	private int port;
	private boolean messagingPreservation;
	
	private boolean jsonInput = true;
	private boolean jsonOutput = true;
	
	
	public String getHost() {
		return host;
	}
	@ModuleOption("setHost")
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	@ModuleOption("setPort")
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isMessagingPreservation() {
		return messagingPreservation;
	}
	@ModuleOption("setMessagingPreservation")
	public void setMessagingPreservation(boolean messagingPreservation) {
		this.messagingPreservation = messagingPreservation;
	}
	public boolean isJsonInput() {
		return jsonInput;
	}
	@ModuleOption("setJsonInput")
	public void setJsonInput(boolean jsonInput) {
		this.jsonInput = jsonInput;
	}
	public boolean isJsonOutput() {
		return jsonOutput;
	}
	@ModuleOption("setJsonOutput")
	public void setJsonOutput(boolean jsonOutput) {
		this.jsonOutput = jsonOutput;
	}
	@Override
	public String[] profilesToActivate() {
		String[] result = new String[2];
		result[0] = jsonInput ? use_json_input : dont_use_json_input;
		result[1] = jsonOutput ? use_json_output : dont_use_json_output;
		return result;
	}
}
