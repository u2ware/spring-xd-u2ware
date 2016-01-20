package io.github.u2ware.integration.netty.x;

import java.io.Serializable;

public class ElevatorHpnrtResponse implements Serializable{

	private static final long serialVersionUID = 3559048063821747436L;

	private String id;
	private Object value;
	private String idDesc;
	private String valueDesc;
	
	public ElevatorHpnrtResponse(){
		
	}

	public ElevatorHpnrtResponse(String id, Object value, String idDesc, String valueDesc){
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
		return "ElevatorHpnrtResponse [id=" + id + ", value=" + value
				+ ", idDesc=" + idDesc + ", valueDesc=" + valueDesc + "]";
	}
}
