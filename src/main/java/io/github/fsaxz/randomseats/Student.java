package io.github.fsaxz.randomseats;

public class Student {
	private String name;
	private Sex sex;

	public Student(String name, Sex sex) {
		this.name = name;
		this.sex = sex;
	}

	public String getName() {
		return this.name;
	}

	public Sex getSex() {
		return this.sex;
	}
}
