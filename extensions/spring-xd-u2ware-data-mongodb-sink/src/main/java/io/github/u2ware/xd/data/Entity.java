package io.github.u2ware.xd.data;


public class Entity {

	private Object id;
	private String name;
	private Object value;
	private String datetime;
	private Object payload;
	private Strategy strategy;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Strategy getStrategy() {
		return strategy;
	}
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	@Override
	public String toString() {
		return "Entity [id=" + id 
				+ ", datetime=" + datetime 
				+ ", value=" + value
				+ ", name=" + name 
				+ ", strategy=" + strategy
				+ ", payload=" + payload + "]";
	}
	public static enum Strategy{
		NOMAL,
		HISTORY,
		ALARM
	}
}
