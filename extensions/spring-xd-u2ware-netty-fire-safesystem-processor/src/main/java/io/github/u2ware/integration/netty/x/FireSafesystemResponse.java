package io.github.u2ware.integration.netty.x;

import java.io.Serializable;

public class FireSafesystemResponse implements Serializable{

	private static final long serialVersionUID = 1325956282725736704L;

	private String id = "alarm";
	private Object value;
	private String name = "화재알람";
	private Long interval = 0l;
	
	public FireSafesystemResponse(){
		
	}

	public FireSafesystemResponse(Object value){
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getInterval() {
		return interval;
	}
	public void setInterval(Long interval) {
		this.interval = interval;
	}

	@Override
	public String toString() {
		return "FireSafesystemResponse [id=" + id + ", value=" + value
				+ ", name=" + name + ", interval=" + interval + "]";
	}
}
