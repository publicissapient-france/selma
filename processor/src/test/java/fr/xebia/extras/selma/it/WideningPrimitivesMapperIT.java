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
package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.EnumIn;
import fr.xebia.extras.selma.beans.EnumOut;
import fr.xebia.extras.selma.it.mappers.SimpleMapper;
import fr.xebia.extras.selma.it.mappers.WideningMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 *
 */
@Compile(withClasses = WideningMapper.class)
public class WideningPrimitivesMapperIT extends IntegrationTestBase {

    @Test
    public void mapper_should_convert_int_to_long() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);

        long res = mapper.convertIntToLong(3);

        assertEquals(3, res);
    }

    @Test
    public void mapper_should_convert_int_to_float() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        float res = mapper.convertIntToFloat(3);
        assertThat(res, is(3f));
    }

    @Test
    public void mapper_should_convert_byte_to_int() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        int res = mapper.convertByteToInt((byte)3);
        assertThat(res, is((int)((byte)3)));
    }

    @Test
    public void mapper_should_convert_short_to_double() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        double res = mapper.convertShortToDouble((short) 3);
        assertThat(res, is((double)((short)3)));
    }

    @Test
    public void mapper_should_convert_byte_to_long() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        long res = mapper.convertByteToLong((byte) 3);
        assertThat(res, is((long)((byte)3)));
    }

    @Test
    public void mapper_should_convert_long_to_float() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        float res = mapper.convertLongToFloat(3l);
        assertThat(res, is(3f));
    }

    @Test
    public void mapper_should_convert_char_to_int() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        int res = mapper.convertCharToInt('a');
        assertThat(res, is((int)'a'));
    }

    @Test
    public void mapper_should_convert_char_to_double() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        double res = mapper.convertCharToDouble('a');
        assertThat(res, is((double)'a'));
    }

    @Test
    public void mapper_should_convert_Byte_to_float() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        float res = mapper.convertBByteToFloat(Byte.valueOf((byte)0));
        assertThat(res, is((float)0));
    }

    @Test
    public void mapper_should_convert_byte_to_Float() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        Float res = mapper.convertByteToBFloat((byte)0);
        assertThat(res, is((float)0));
    }

    @Test
    public void mapper_should_convert_Short_to_Integer() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);
        Integer res = mapper.convertBShortToBInteger(new Short((short)0));
        assertThat(res, is(0));
    }


    @Test
    public void mapper_should_convert_BIntegerArray_to_BLongArray() throws Exception {

        WideningMapper mapper = Selma.getMapper(WideningMapper.class);


        Long[] res = mapper.convertBIntegerArrayToBLongArray(new Integer[]{0,12, null, 34});
        assertThat(res, is(new Long[]{0l, 12l, null, 34l}));
    }

}
