package io.github.u2ware.xd.data;


public class Entity {

	private Object id;
	private Object value;
	private String datetime;
	private Object status;

	private String name;
	private String criteria; //value == 1, value > 1111 , value < 111 , value > 111 && value < 222 
	private Long interval; //1000, 60*60
	
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
	public Object getStatus() {
		return status;
	}
	public void setStatus(Object status) {
		this.status = status;
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
				+ ", datetime=" + datetime 
				+ ", value=" + value 
				+ ", status=" + status
				+ ", name=" + name 
				+ ", criteria=" + criteria
				+ ", interval="+ interval 
				+ "]";
	}
}
