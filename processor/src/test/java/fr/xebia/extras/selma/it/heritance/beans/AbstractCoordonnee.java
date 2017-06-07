/*
 * Copyright 2013  Séven Le Mesle
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
package fr.xebia.extras.selma.it.heritance.beans;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by slemesle on 06/06/2017.
 */
public class AbstractCoordonnee {

    private String id;
    private Date updateDate;
    private String type;
    private boolean npai;

    public AbstractCoordonnee(String id){
        this.id = id;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNpai() {
        return npai;
    }

    public void setNpai(boolean npai) {
        this.npai = npai;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = (String) id;
    }
}
