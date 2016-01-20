package io.github.u2ware.integration.netty.x;

public class LightingJunghoResponse {

	private String id;
	private Object value;
	
	public LightingJunghoResponse(){
		
	}
	
	public LightingJunghoResponse(String id, Object value) {
		this.id = id;
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
		return "LightingJunghoResponse [id=" + id + ", value=" + value + "]";
	}
}
