package io.github.u2ware.xd.repository;

import org.springframework.xd.module.options.spi.ModuleOption;
import org.springframework.xd.module.options.spi.ProfileNamesProvider;

public class MongodbOptions implements ProfileNamesProvider{

	private static final String use_json_input = "use_json_input";
	private static final String dont_use_json_input = "dont_use_json_input";
	
	private String databaseName;
	private String host;
	private int port = 27017;
	private String idExpression;
	private String valueExpression;
	private boolean jsonInput = true;
	
	public String getDatabaseName() {
		return databaseName;
	}
	@ModuleOption("setDatabaseName")
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
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
	public String getIdExpression() {
		return idExpression;
	}
	@ModuleOption("setIdExpression")
	public void setIdExpression(String idExpression) {
		this.idExpression = idExpression;
	}
	public String getValueExpression() {
		return valueExpression;
	}
	@ModuleOption("setValueExpression")
	public void setValueExpression(String valueExpression) {
		this.valueExpression = valueExpression;
	}
	public boolean isJsonInput() {
		return jsonInput;
	}
	@ModuleOption("setJsonInput")
	public void setJsonInput(boolean jsonInput) {
		this.jsonInput = jsonInput;
	}

	@Override
	public String[] profilesToActivate() {
		String[] result = new String[1];
		result[0] = jsonInput ? use_json_input : dont_use_json_input;
		return result;
	}
}
