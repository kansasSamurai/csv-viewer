package org.jwellman.csvviewer.models;

/**
 * An example data model for prototyping.
 * This class can probably be removed once I remove the prototyping
 * in the DataBrowser class; particularly making the code for 
 * numbercellrenderer an option.
 * 
 * @author rwellman
 *
 */
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