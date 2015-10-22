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
package fr.xebia.extras.selma.it.custom.fields;

import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by slemesle on 21/11/14.
 */
@Compile(withClasses = {
        FailingEmbeddedFieldToUpperFieldMapper.class
}, shouldFail = true)
public class FailingEmbeddedFieldToUpperFieldInMapsIT extends IntegrationTestBase {


    public static final int EXPECTED_ERROR_COUNT = 7;

    @Test
    public void given_a_mapper_with_custom_embeded_field_to_upper_bad_field_compilation_should_fail() throws Exception {
        assertCompilationError(FailingEmbeddedFieldToUpperFieldMapper.class, "ProposalDto asProposalDto(Proposal proposal);", "Bad custom field to field mapping: setter for field passengerager is missing in destination bean fr.xebia.extras.selma.beans.ProposalDto");
    //   Assert.assertEquals(EXPECTED_ERROR_COUNT, compilationErrorCount());
    }

    @Test
    public void given_a_mapper_with_custom_embeded_field_to_embedded_field_compilation_should_fail() throws Exception {
        assertCompilationError(FailingEmbeddedFieldToUpperFieldMapper.class, "ProposalDto asProposalDto2(Proposal proposal);", "Bad custom field to field mapping: both source and destination can not be embedded !");
     //   Assert.assertEquals(EXPECTED_ERROR_COUNT, compilationErrorCount());
    }

    @Test
    public void given_a_mapper_with_custom_embeded_bad_last_field_to_upper_field_compilation_should_fail() throws Exception {
        assertCompilationError(FailingEmbeddedFieldToUpperFieldMapper.class, "ProposalDto asProposalDto3(Proposal proposal);", "Bad custom field to field mapping: field in.getPassenger().ager from source bean fr.xebia.extras.selma.beans.Proposal has no getter !");
       // Assert.assertEquals(EXPECTED_ERROR_COUNT, compilationErrorCount());
    }

    @Test
    public void given_a_mapper_with_custom_embeded_bad_middle_field_to_upper_field_compilation_should_fail() throws Exception {
        assertCompilationError(FailingEmbeddedFieldToUpperFieldMapper.class, "ProposalDto asProposalDto4(Proposal proposal);", "Bad custom field to field mapping: field in.getPassenger().ager from source bean fr.xebia.extras.selma.beans.Proposal has no getter !");
      //  Assert.assertEquals(EXPECTED_ERROR_COUNT, compilationErrorCount());
    }

    @Test
    public void given_a_mapper_with_custom_upper_field_to_bad_embedded_field_compilation_should_fail() throws Exception {
        assertCompilationError(FailingEmbeddedFieldToUpperFieldMapper.class, "Proposal asProposalBadDestination(ProposalDto proposal);", "Bad custom field to field mapping: field out.getPassenger().ager from destination bean fr.xebia.extras.selma.beans.Proposal has no setter !");
        //Assert.assertEquals(EXPECTED_ERROR_COUNT, compilationErrorCount());
    }

    @Test
    public void given_a_mapper_with_custom_bad_upper_field_to_embedded_field_compilation_should_fail() throws Exception {
        assertCompilationWarning(FailingEmbeddedFieldToUpperFieldMapper.class, "Proposal asProposalBadSource(ProposalDto proposal);", "Custom @Field({\"passenger.age\",\"passengerager\"}) mapping is never used !");
        // Assert.assertEquals(EXPECTED_ERROR_COUNT, compilationErrorCount());
    }

}
