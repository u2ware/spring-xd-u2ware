package io.github.u2ware.xd.data;

public class Entity {

	private Object id;
	private Object value;
	private Long timestamp;
	private String payload;
	
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	@Override
	public String toString() {
		return "Entity [id=" + id + ", value=" + value + ", timestamp="
				+ timestamp + ", payload=" + payload + "]";
	}
}
