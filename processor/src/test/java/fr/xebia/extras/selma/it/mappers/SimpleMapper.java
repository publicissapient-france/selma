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
public interface SimpleMapper {

    String convert(String in);

    int convert(int in);

    boolean convert(boolean in);

    byte convert(byte in);

    char convert(char in);

    Integer convert(Integer in);

    char[] convert(char[] in);

    Character[] convert(Character[] in);

    String[] convert(String[] in);

    List<String> convert(List<String> in);

    HashSet<String> convert(HashSet<String> in);

    Map<String, String> convert(Map<String, String> in);

    EnumOut convert(EnumIn in);

    String[][] convert(String[][] in);

    String[][][] convert(String[][][] in);

    HashSet<String> convertSetToHashSet(Set<String> in);

    Set<String> convertLnkedHashSetToSet(LinkedHashSet<String> in);

    HashMap<String, Date> convertStringToDateMap(TreeMap<String, Date> in);

    Map<BigInteger, Date> convertBigIntToDateMap(Map<BigInteger, Date> in);

    BigInteger convertBigInt(BigInteger in);

    Date convertDate(Date in);

    UUID convertUUID(UUID in);

    int convertIntegerToInt(Integer in);

    BigDecimal convertBigDecimal(BigDecimal in);

    Integer convertIntToInteger(int in);
}
