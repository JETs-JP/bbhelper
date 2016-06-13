package com.oracle.poco.bbhelper.model;

public class Person {

    private String name;
    private String address;
    // TODO: linkを埋める仕組みを実装する
    private String link;

    public Person() {
        super();
    }

    public Person(String name, String address, String link) {
        super();
        this.name = name;
        this.address = address;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", address=" + address + ", link=" + link + "]";
    }

}
