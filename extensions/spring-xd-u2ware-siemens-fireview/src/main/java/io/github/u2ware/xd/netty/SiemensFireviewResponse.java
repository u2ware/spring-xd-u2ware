package io.github.u2ware.xd.netty;

import io.github.u2ware.integration.common.BuildingAutomationSystemData;

public class SiemensFireviewResponse implements BuildingAutomationSystemData{

	private static final long serialVersionUID = 1325956282725736704L;

	private String id = "alarm";
	private Object value;
	private Byte opCode;
	private String opName;
	
	public SiemensFireviewResponse(){
		
	}

	public SiemensFireviewResponse(String value, Byte opCode, String opName){
		this.id = "alarm";
		this.value = value;
		this.opCode = opCode;
		this.opName = opName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public Byte getOpCode() {
		return opCode;
	}

	public void setOpCode(Byte opCode) {
		this.opCode = opCode;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	@Override
	public String toString() {
		return "SiemensFireviewResponse [id=" + id + ", value=" + value
				+ ", opCode=" + opCode + ", opName=" + opName + "]";
	}
}
