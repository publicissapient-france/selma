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
package fr.xebia.extras.selma.it.aggregation;

import fr.xebia.extras.selma.beans.*;

/**
 * Created by slemesle on 24/04/2017.
 */
public class AggregatedInterceptor {


    public static final int SALARY_INC = 10000;

    /**
     * Simply intercept in and out person after mapping process
     *
     */
    public void intercept(FirstBean first, SecondBean second, AggregatedBean aggregatedBean) {

        aggregatedBean.setSalary(second.getSalary() + SALARY_INC);
    }
}
