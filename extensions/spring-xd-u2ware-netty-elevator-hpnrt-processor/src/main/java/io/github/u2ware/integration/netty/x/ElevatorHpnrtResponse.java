package io.github.u2ware.integration.netty.x;

import java.io.Serializable;

public class ElevatorHpnrtResponse implements Serializable{

	private static final long serialVersionUID = 3559048063821747436L;

	private String id;
	private Object value;
	private String name;
	private Long interval;
	
	public ElevatorHpnrtResponse(){
		
	}

	public ElevatorHpnrtResponse(String id, Object value, String name, Long interval){
		this.id = id;
		this.value = value;
		this.name = name;
		this.interval = interval;
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
		return "ElevatorHpnrtResponse [id=" + id + ", value=" + value
				+ ", name=" + name + ", interval="
				+ interval + "]";
	}

}
