package com.example;

import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "class")
@XmlAccessorType(XmlAccessType.FIELD)
public class School {

    ArrayList<Student> student;

    public ArrayList<Student> getStudents() {
        return this.student;
    }

    public void setStudents(ArrayList<Student> students) {
        this.student = students;
    }
}
