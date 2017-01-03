/*
 * Copyright 2013 Xebia and Séven Le Mesle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package fr.xebia.extras.selma.beans;

import java.util.Collection;
import java.util.Date;

/**
 *
 */
public class PersonIn {


    public byte[] keyStore;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private int age;
    private boolean male;
    private Long[] indices;
    private Collection<String> tags;
    private EnumIn enumIn;
    private AddressIn address;
    private AddressIn addressBis;
    private Boolean natural;

    public byte[] getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(byte[] keyStore) {
        this.keyStore = keyStore;
    }

    public EnumIn getEnumIn() {
        return enumIn;
    }

    public void setEnumIn(EnumIn enumIn) {
        this.enumIn = enumIn;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }


    public Long[] getIndices() {
        return indices;
    }

    public void setIndices(Long[] indices) {
        this.indices = indices;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public AddressIn getAddress() {
        return address;
    }

    public void setAddress(AddressIn address) {
        this.address = address;
    }

    public AddressIn getAddressBis() {
        return addressBis;
    }

    public void setAddressBis(AddressIn addressBis) {
        this.addressBis = addressBis;
    }

    public void setNatural(Boolean natural) {
        this.natural = natural;
    }

    public Boolean isNatural() {
        return natural;
    }
}
