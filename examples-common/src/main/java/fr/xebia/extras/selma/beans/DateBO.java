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
package fr.xebia.extras.selma.beans;

/**
 * Created by slemesle on 02/12/14.
 */
public class DateBO {

    private String date;

    public DateBO(String date) {
        this.date = date;
    }

    @Override public String toString() {
        return date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateBO dateBO = (DateBO) o;

        if (date != null ? !date.equals(dateBO.date) : dateBO.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return date != null ? date.hashCode() : 0;
    }
}