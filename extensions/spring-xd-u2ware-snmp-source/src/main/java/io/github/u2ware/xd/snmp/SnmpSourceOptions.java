package io.github.u2ware.xd.snmp;

import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ProfileNamesProvider;

public class SnmpSourceOptions implements ProfileNamesProvider{


	private static final String use_splitter = "use_splitter";
	private static final String dont_use_splitter = "dont_use_splitter";
	
	private static final String use_json_output = "use_json_output";
	private static final String dont_use_json_output = "dont_use_json_output";

	private int port;
	private String mibFile;
	private String requestSupport;
	
	private int fixedDelay = 10000;
	private boolean split = true;
	private boolean jsonOutput = true;
	
	public int getPort() {
		return port;
	}
	@ModuleOption("setPort")
	public void setPort(int port) {
		this.port = port;
	}
	public String getMibFile() {
		return mibFile;
	}
	@ModuleOption("setMibFile")
	public void setMibFile(String mibFile) {
		this.mibFile = mibFile;
	}
	public String getRequestSupport() {
		return requestSupport;
	}
	@ModuleOption("setRequestSupport")
	public void setRequestSupport(String requestSupport) {
		this.requestSupport = requestSupport;
	}
	public int getFixedDelay() {
		return fixedDelay;
	}
	@ModuleOption("setFixedDelay")
	public void setFixedDelay(int fixedDelay) {
		this.fixedDelay = fixedDelay;
	}
	public boolean isSplit() {
		return split;
	}
	@ModuleOption("setSplit")
	public void setSplit(boolean split) {
		this.split = split;
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
		result[0] = split ? use_splitter : dont_use_splitter;
		result[1] = jsonOutput ? use_json_output : dont_use_json_output;
		return result;
	}
}
