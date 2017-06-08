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
package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.EnumIn;
import fr.xebia.extras.selma.beans.EnumOut;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 *
 */
@Mapper
public interface WideningMapper {

    long convertIntToLong(int in);

    float convertIntToFloat(int in);

    int convertByteToInt(byte in);

    double convertShortToDouble(short in);

    long convertByteToLong(byte in);

    float convertLongToFloat(long in);

    int convertCharToInt(char in);

    double convertCharToDouble(char in);

    float convertBByteToFloat(Byte in);

    Float convertByteToBFloat(byte in);

    Integer convertBShortToBInteger(Short in);

    Container<Integer> convertCBShortToCBInteger(Container<Short> in);

    Long[] convertBIntegerArrayToBLongArray(Integer[] in);



    class Container<T> {
        private T value;

        public Container(){}

        public Container(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

}
