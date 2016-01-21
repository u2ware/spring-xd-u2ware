package io.github.u2ware.integration.netty.x;

import java.io.Serializable;

public class FireSiemensResponse implements Serializable{

	private static final long serialVersionUID = 1325956282725736704L;

	private String id = "alarm";
	private Byte value;
	private String message;
	
	public FireSiemensResponse(){
		
	}

	public FireSiemensResponse(Byte value, String message){
		this.id = "alarm";
		this.value = value;
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Byte getValue() {
		return value;
	}
	public void setValue(Byte value) {
		this.value = value;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "FireSiemensResponse [id=" + id + ", value=" + value
				+ ", message=" + message + "]";
	}

	
}
