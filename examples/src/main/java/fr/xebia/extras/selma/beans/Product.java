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

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 26/11/2013
 * Time: 23:48
 * To change this template use File | Settings | File Templates.
 */
public class Product {

    private Map<String, String> logisticMap;
    private String code;
    private String label;
    private double price;
    private Set<String> tags;
    private ProductType type;

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getLogisticMap() {
        return logisticMap;
    }

    public void setLogisticMap(Map<String, String> logisticMap) {
        this.logisticMap = logisticMap;
    }
}
