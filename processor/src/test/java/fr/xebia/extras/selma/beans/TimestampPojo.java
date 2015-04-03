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

import java.util.Map;

/**
 * Created by slemesle on 03/04/15.
 */
public class TimestampPojo {

    private String myString;
    private Map<String, Long> dates;
    private Map<String, Long> datesTwo;
    private Long aDate;

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public Map<String, Long> getDatesTwo() {
        return datesTwo;
    }

    public void setDatesTwo(Map<String, Long> datesTwo) {
        this.datesTwo = datesTwo;
    }

    public Long getaDate() {
        return aDate;
    }

    public void setaDate(long aDate) {
        this.aDate = aDate;
    }

    public Map<String, Long> getDates() {
        return dates;
    }

    public void setDates(Map<String, Long> dates) {
        this.dates = dates;
    }
}
