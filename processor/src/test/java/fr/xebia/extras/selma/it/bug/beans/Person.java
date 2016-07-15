package fr.xebia.extras.selma.it.bug.beans;

import java.util.Collections;
import java.util.List;

public class Person {
    private String name;

    private Address address;

    private List<Address> additionalAddresses = Collections.emptyList();

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Address> getAdditionalAddresses() {
        return additionalAddresses;
    }

    public void setAdditionalAddresses(List<Address> additionalAddresses) {
        this.additionalAddresses = additionalAddresses;
    }
}
