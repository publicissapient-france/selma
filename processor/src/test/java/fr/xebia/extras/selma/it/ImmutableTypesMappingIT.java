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
package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.TicketIn;
import fr.xebia.extras.selma.beans.TicketOut;
import fr.xebia.extras.selma.beans.TicketTocken;
import fr.xebia.extras.selma.it.mappers.ImmutableTypesMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by slemesle on 17/10/2014.
 */
@Compile(withClasses = {ImmutableTypesMapper.class})
public class ImmutableTypesMappingIT extends IntegrationTestBase {

    @Test
    public void given_mapper_with_immutable_type_when_mapp_then_immutable_type_should_be_copied_by_reference() {

        TicketTocken tocken = new TicketTocken("12345432", new Date());
        TicketIn source = new TicketIn("toto", "#T0t0;", tocken);

        ImmutableTypesMapper immutableTypesMapper = Selma.builder(ImmutableTypesMapper.class).build();

        TicketOut ticketOut = immutableTypesMapper.asTicketOut(source);

        Assert.assertThat(ticketOut.getTocken(), CoreMatchers.is(tocken));
    }

    @Test
    public void given_mapper_with_not_used_immutable_type_when_compiled_then_report_warning_unused() throws Exception {

        assertCompilationWarning(ImmutableTypesMapper.class, "public interface ImmutableTypesMapper {", "Immutable class \"java.lang.Byte\" is never used");
    }

}