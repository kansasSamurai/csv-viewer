package org.jwellman.csvviewer.models;

public class Person {
 
    private int age;
    private String name;
 
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
 
    public int getAge() { return age; }
 
    public String getName() { return name; }
    
}