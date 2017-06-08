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
package fr.xebia.extras.selma.it.custom.interceptor.beans;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by slemesle on 08/06/2017.
 */
public class CustomDateTimeInterceptor {


    public void intercept(ValidationJson in, Validation out) {
        final Integer eventDateStamp = in.getEventDateStamp();
        final Integer eventTimeStamp = in.getEventTimeStamp();
        if (null != eventDateStamp && null != eventTimeStamp) {
            final LocalDate date = LocalDate.now().plusDays(eventDateStamp);
            final LocalTime time = LocalTime.now().plusMinutes(eventTimeStamp);
            final Calendar validationDate = GregorianCalendar.getInstance();
            validationDate.setTime(date.toDate());
            validationDate.setTimeZone(TimeZone.getDefault());
            out.setValidationDate(validationDate);
        }
    }

}
