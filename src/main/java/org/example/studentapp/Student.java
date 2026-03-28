package org.example.studentapp;

// This class represents a single Student entity (data model)
public class Student {

    // Student properties (fields)
    private String name;
    private int age;
    private String course;

    // Constructor used to create a new Student object
    Student(String name, int age, String course) {
        this.name = name;
        this.age = age;
        this.course = course;
    }

    // Getter method to access student's name
    public String getName() {
        return name;
    }

    // Getter method to access student's age
    public int getAge() {
        return age;
    }

    // Getter method to access student's course
    public String getCourse() {
        return course;
    }
}