package io.github.u2ware.xd.ibs.person;

public class Person {
	
	private Long id;
	private String name;
	private String birth;
	
	public Person(){
	}
	public Person(Long id, String name, String birth){
		this.id= id;
		this.name = name;
		this.birth = birth;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	
	
	
}
