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
package fr.xebia.extras.selma;

/**
 * Created by slemesle on 02/06/2014.
 */
public class SelmaException extends RuntimeException {
    public SelmaException(Exception e) {
        super(e);
    }

    public SelmaException(Exception e, String messageFormat, Object... params) {
        super(String.format(messageFormat, params), e);
    }

    public SelmaException(String messageFormat, Object... params) {
        super(String.format(messageFormat, params));
    }
}
