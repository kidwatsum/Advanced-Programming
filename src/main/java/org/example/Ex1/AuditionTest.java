package org.example.Ex1;

import java.util.*;
import java.util.stream.Collectors;

class Person implements Comparable<Person>{
    private String city;
    private String code;
    private String name;
    private int age;

    public Person(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n",code,name,age);
    }

    @Override
    public int compareTo(Person o) {
        return Comparator.comparing(Person::getName).thenComparing(Person::getAge).thenComparing(Person::getCode).compare(this,o);
    }
}

class Audition{
    private HashMap<String,List<Person>> participants;

    public Audition() {
        participants=new HashMap<>();
    }
    public void addParticipant(String city,String code,String name,int age){
        participants.putIfAbsent(city,new ArrayList<>());
        boolean codeExists=participants.get(city).stream().anyMatch(person -> person.getCode().equals(code));
        if(!codeExists){
            Person p=new Person(city, code, name, age);
            participants.get(city).add(p);
        }


    }
    public void listByCity(String city){
        if(participants.containsKey(city)){
            List<Person> people=participants.get(city).stream().sorted().collect(Collectors.toList());
            for(Person p:people){
                System.out.print(p);
            }

        }
    }

}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
