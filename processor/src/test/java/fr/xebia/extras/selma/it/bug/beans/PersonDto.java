package fr.xebia.extras.selma.it.bug.beans;

import java.util.Collections;
import java.util.List;

public class PersonDto {
    private String name;
    private String addressStreet;
    private String addressZipCode;
    private String addressCity;
    private String addressCountry;

    private List<AddressDto> additionalAddresses = Collections.emptyList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressZipCode() {
        return addressZipCode;
    }

    public void setAddressZipCode(String addressZipCode) {
        this.addressZipCode = addressZipCode;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public List<AddressDto> getAdditionalAddresses() {
        return additionalAddresses;
    }

    public void setAdditionalAddresses(List<AddressDto> additionalAddresses) {
        this.additionalAddresses = additionalAddresses;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }
}
