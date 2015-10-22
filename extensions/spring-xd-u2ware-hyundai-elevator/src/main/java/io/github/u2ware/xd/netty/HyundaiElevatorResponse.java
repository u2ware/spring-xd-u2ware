package io.github.u2ware.xd.netty;

import java.io.Serializable;

public class HyundaiElevatorResponse implements Serializable{

	private static final long serialVersionUID = 1325956282725736704L;

	private String id;
	private Object value;
	private String name;
	private String description;
	
	public HyundaiElevatorResponse(){
		
	}

	public HyundaiElevatorResponse(String id, String value, String name, String description){
		this.id = id;
		this.value = value;
		this.name = name;
		this.description = description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "HyundaiElevatorResponse [id=" + id + ", value=" + value
				+ ", name=" + name + ", description=" + description + "]";
	}
}
