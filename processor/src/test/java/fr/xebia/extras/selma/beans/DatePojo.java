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

import java.util.Date;
import java.util.Map;

/**
 * Created by slemesle on 03/04/15.
 */
public class DatePojo {
    private String myString;
    private Map<String, Date> dates;
    private Map<String, Date> datesTwo;
    private Date aDate;

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public Map<String, Date> getDatesTwo() {
        return datesTwo;
    }

    public void setDatesTwo(Map<String, Date> datesTwo) {
        this.datesTwo = datesTwo;
    }

    public Date getaDate() {
        return aDate;
    }

    public void setaDate(Date aDate) {
        this.aDate = aDate;
    }

    public Map<String, Date> getDates() {
        return dates;
    }

    public void setDates(Map<String, Date> dates) {
        this.dates = dates;
    }
}
