package io.github.u2ware.xd.mongodb.test;


public class Point {

	private String id;
	private Object value;
	private Object other;
	
	public Point(){
		
	}
	public Point(String id, Object value){
		this.id = id;
		this.value = value;
	}
	public Point(String id, Object value, Object other){
		this.id = id;
		this.value = value;
		this.other = other;
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
	public Object getOther() {
		return other;
	}
	public void setOther(Object other) {
		this.other = other;
	}
	
}
