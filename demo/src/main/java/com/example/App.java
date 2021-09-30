package com.example;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) {

        JAXBContext jaxbContext = null;
        try {

            // Normal JAXB RI
            jaxbContext = JAXBContext.newInstance(School.class);

            // EclipseLink MOXy needs jaxb.properties at the same package with School.class
            // Alternative, I prefer define this via eclipse JAXBContextFactory manually.
            //jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
            //        .createContext(new Class[]{School.class}, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            //jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            
            // Idk why doesn't this work
            //jaxbMarshaller.setProperty("com.sun.xml.bind.xmlHeaders", "\n<!-- Generated automatically. Don't change it. -->");

            Student obj1 = new Student();
            obj1.setId("201134441110");
            obj1.setAge(21);
            obj1.setName("Alberto");

            Student obj2 = new Student();
            obj2.setId("201134441116");
            obj2.setAge(22);
            obj2.setName("Patricia");

            Student obj3 = new Student();
            obj3.setId("201134441210");
            obj3.setAge(21);
            obj3.setName("Luis");

            ArrayList<Student> lista = new ArrayList<>(Arrays.asList(obj1, obj2, obj3));
            
            School school = new School();
            school.setStudents(lista);

            jaxbMarshaller.marshal(school, new File("C:\\Users\\Josphze\\Desktop\\IS\\demo\\fruit.xml"));
            
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
