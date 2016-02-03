package io.github.u2ware.integration.netty.x;

import java.io.Serializable;

public class ElevatorHyundaiResponse implements Serializable{

	private static final long serialVersionUID = 1325956282725736704L;

	private String id;
	private Object value;
	private String name;
	private String state;
	
	public ElevatorHyundaiResponse(){
		
	}

	public ElevatorHyundaiResponse(String id, Object value, String name, String state){
		this.id = id;
		this.value = value;
		this.name = name;
		this.state = state;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ElevatorHyundaiResponse [id=" + id + ", value=" + value
				+ ", name=" + name + ", state=" + state + "]";
	}
}
