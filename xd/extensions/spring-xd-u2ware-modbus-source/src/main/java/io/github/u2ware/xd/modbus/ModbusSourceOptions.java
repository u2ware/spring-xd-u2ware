package io.github.u2ware.xd.modbus;

import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ProfileNamesProvider;

public class ModbusSourceOptions implements ProfileNamesProvider{

	private static final String use_splitter = "use_splitter";
	private static final String dont_use_splitter = "dont_use_splitter";
	
	private static final String use_json_output = "use_json_output";
	private static final String dont_use_json_output = "dont_use_json_output";

	private String host;
	private int port = 502;
	private int unitId;
	private int functionCode;
	private int offset;
	private int count;
	private int fixedDelay = 10000;
	private boolean split = true;
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
	public int getUnitId() {
		return unitId;
	}
	@ModuleOption("setUnitId")
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getFunctionCode() {
		return functionCode;
	}
	@ModuleOption("setFunctionCode")
	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
	}
	public int getOffset() {
		return offset;
	}
	@ModuleOption("setOffset")
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getCount() {
		return count;
	}
	@ModuleOption("setCount")
	public void setCount(int count) {
		this.count = count;
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
