package io.github.u2ware.xd.ibs;

public class Data {

	private String id;
	private Object value;
	private Long timestamp;
	private Object payload;
	
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
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	@Override
	public String toString() {
		return "IBSData [id=" + id + ", value=" + value + ", timestamp="
				+ timestamp + ", payload=" + payload + "]";
	}
}
