package com.oracle.poco.bbhelper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {

    private static final String ARIA_URL_FORMAT =
            "https://people.us.oracle.com/pls/oracle/f"
            + "?p=8000:1:12962344332185:::RP,RIR:P1_SEARCH,"
            + "P1_SEARCH_TYPE:" + "%s" + ",People";

    private static final String SCHEME_MAILTO = "mailto:";

    private final String name;
    private final String address;
    private final String link;

    @JsonCreator
    public Person(@JsonProperty("name") String name, @JsonProperty("address") String address) {
        super();
        this.name = name;
        this.address = address;

        if (address == null || address.length() == 0) {
            this.link = null;
        } else if (address.startsWith(SCHEME_MAILTO)) {
            this.link = String.format(
                    ARIA_URL_FORMAT, address.substring(SCHEME_MAILTO.length()).split("@")[0]);
        } else {
            this.link = String.format(ARIA_URL_FORMAT, address.split("@")[0]);
        }
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!getName().equals(person.getName())) return false;
        if (!getAddress().equals(person.getAddress())) return false;
        return getLink().equals(person.getLink());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getAddress().hashCode();
        result = 31 * result + getLink().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

}
