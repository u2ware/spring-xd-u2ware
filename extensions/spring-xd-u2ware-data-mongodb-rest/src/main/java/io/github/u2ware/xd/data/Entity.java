package io.github.u2ware.xd.data;


public class Entity {

	private Object id;
	private Object value;
	private String datetime;
	private Object payload;

	private String name;
	private String criteria; //value == 1, value > 1111 , value < 111 , value < 111 && value < 111 //
	private Long interval; // 0 , 1000, 60*60
	
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
	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public Long getInterval() {
		return interval;
	}
	public void setInterval(Long interval) {
		this.interval = interval;
	}
	@Override
	public String toString() {
		return "Entity [id=" + id 
				+ ", value=" + value 
				+ ", datetime=" + datetime 
				+ ", name=" + name 
				+ ", criteria=" + criteria
				+ ", interval="+ interval 
				+ ", payload=" + payload
				+ "]";
	}
}
