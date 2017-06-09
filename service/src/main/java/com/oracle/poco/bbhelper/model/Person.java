package com.oracle.poco.bbhelper.model;

public class Person {

    private static final String ARIA_URL_FORMAT =
            "https://people.us.oracle.com/pls/oracle/f"
            + "?p=8000:1:12962344332185:::RP,RIR:P1_SEARCH,"
            + "P1_SEARCH_TYPE:" + "%s" + ",People";

    private static final String SCHEME_MAILTO = "mailto:";

    private final String name;
    private final String address;
    private final String link;

    public Person(String name, String address) {
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
    public String toString() {
        return "Person [name=" + name + ", address=" + address + ", link=" + link + "]";
    }

}
