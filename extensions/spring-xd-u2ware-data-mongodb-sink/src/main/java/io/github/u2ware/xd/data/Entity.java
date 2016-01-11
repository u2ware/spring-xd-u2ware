package io.github.u2ware.xd.data;


public class Entity {

	private Object id;
	private Object value;
	private String datetime;
	private Object payload;
	
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
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	@Override
	public String toString() {
		return "Entity [id=" + id + ", value=" + value + ", datetime="
				+ datetime + ", payload=" + payload + "]";
	}
}
