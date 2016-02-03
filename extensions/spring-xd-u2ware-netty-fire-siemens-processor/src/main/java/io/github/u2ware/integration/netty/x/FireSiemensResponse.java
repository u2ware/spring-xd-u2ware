package io.github.u2ware.integration.netty.x;

import java.io.Serializable;

public class FireSiemensResponse implements Serializable{

	private static final long serialVersionUID = 1325956282725736704L;

	private String id = "alarm";
	private Object value;
	
	public FireSiemensResponse(){
		
	}

	public FireSiemensResponse(String value){
		this.id = "alarm";
		this.value = value;
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

	@Override
	public String toString() {
		return "FireSiemensResponse [id=" + id + ", value=" + value+ "]";
	}

	
}
