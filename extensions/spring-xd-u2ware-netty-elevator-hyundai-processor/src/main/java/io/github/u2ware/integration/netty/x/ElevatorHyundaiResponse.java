package io.github.u2ware.integration.netty.x;

import java.io.Serializable;

public class ElevatorHyundaiResponse implements Serializable{

	private static final long serialVersionUID = 1325956282725736704L;

	private String id;
	private Object value;
	private String idDesc;
	private String valueDesc;
	
	public ElevatorHyundaiResponse(){
		
	}

	public ElevatorHyundaiResponse(String id, Object value, String idDesc, String valueDesc){
		this.id = id;
		this.value = value;
		this.idDesc = idDesc;
		this.valueDesc = valueDesc;
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
	public String getIdDesc() {
		return idDesc;
	}
	public void setIdDesc(String idDesc) {
		this.idDesc = idDesc;
	}
	public String getValueDesc() {
		return valueDesc;
	}
	public void setValueDesc(String valueDesc) {
		this.valueDesc = valueDesc;
	}

	@Override
	public String toString() {
		return "ElevatorHyundaiResponse [id=" + id + ", value=" + value
				+ ", idDesc=" + idDesc + ", valueDesc=" + valueDesc + "]";
	}
}
