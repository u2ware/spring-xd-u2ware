package io.github.u2ware.xd.bacnet;

import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ProfileNamesProvider;

public class BacnetProcessorOptions implements ProfileNamesProvider{

	private static final String use_json_input = "use_json_input";
	private static final String dont_use_json_input = "dont_use_json_input";
	
	private static final String use_splitter = "use_splitter";
	private static final String dont_use_splitter = "dont_use_splitter";
	
	private static final String use_json_output = "use_json_output";
	private static final String dont_use_json_output = "dont_use_json_output";

	private int localPort;
	private String remoteAddress;
	private String remoteInstanceNumber;
	private boolean split = true;
	private boolean jsonInput = true;
	private boolean jsonOutput = true;
	
	public int getLocalPort() {
		return localPort;
	}
	@ModuleOption("setLocalPort")
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	public String getRemoteAddress() {
		return remoteAddress;
	}
	@ModuleOption("setRemoteAddress")
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public String getRemoteInstanceNumber() {
		return remoteInstanceNumber;
	}
	@ModuleOption("setRemoteInstanceNumber")
	public void setRemoteInstanceNumber(String remoteInstanceNumber) {
		this.remoteInstanceNumber = remoteInstanceNumber;
	}
	public boolean isSplit() {
		return split;
	}
	@ModuleOption("setSplit")
	public void setSplit(boolean split) {
		this.split = split;
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
		String[] result = new String[3];
		result[0] = split ? use_splitter : dont_use_splitter;
		result[1] = jsonInput ? use_json_input : dont_use_json_input;
		result[2] = jsonOutput ? use_json_output : dont_use_json_output;
		return result;
	}
}
