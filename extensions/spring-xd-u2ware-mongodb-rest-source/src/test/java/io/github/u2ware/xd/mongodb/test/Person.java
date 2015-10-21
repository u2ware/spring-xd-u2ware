package io.github.u2ware.xd.mongodb.test;

public class Person {
	  private String id;
	  private String name;
	  private int age;

	  public Person() {
		  
	  }
	  public Person(String name, int age) {
	    this.name = name;
	    this.age = age;
	  }
	  public Person(String name, int age, String id) {
		    this.id = id;
		    this.name = name;
		    this.age = age;
	  }

	  public String getId() {
	    return id;
	  }
	  public String getName() {
	    return name;
	  }
	  public int getAge() {
	    return age;
	  }

	  @Override
	  public String toString() {
	    return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
	  }
}
