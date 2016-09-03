package com.oracle.poco.bbhelper.model;

public class Person {

    private static final String ARIA_URL_FORMAT =
            "https://people.us.oracle.com/pls/oracle/f"
            + "?p=8000:1:12962344332185:::RP,RIR:P1_SEARCH,"
            + "P1_SEARCH_TYPE:" + "%s" + ",People";

    private static final String SCHEME_MAILTO = "mailto:";

    private String name;
    private String address;
    private String link;

    public Person() {
        super();
    }

    public Person(String name, String address) {
        super();
        this.name = name;
        this.address = address;
        if (address == null || address.length() == 0) {
            this.link = null;
        }
        if (address.startsWith(SCHEME_MAILTO)) {
            address = address.substring(SCHEME_MAILTO.length());
        }
        // TODO 本当は正しいMail Addressのチェックをするのが望ましい
        this.link = String.format(ARIA_URL_FORMAT, address.split("@")[0]);
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
