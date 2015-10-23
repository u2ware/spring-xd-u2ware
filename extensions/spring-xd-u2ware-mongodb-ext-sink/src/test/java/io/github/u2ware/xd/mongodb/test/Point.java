package io.github.u2ware.xd.mongodb.test;


public class Point {

	private String id;
	private Object value;
	private Object others;
	
	public Point(){

	}
	public Point(String id, Object value, Object others){
		this.id = id;
		this.value = value;
		this.others = others;
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
	public Object getOthers() {
		return others;
	}
	public void setOthers(Object others) {
		this.others = others;
	}
	
	
	
}
