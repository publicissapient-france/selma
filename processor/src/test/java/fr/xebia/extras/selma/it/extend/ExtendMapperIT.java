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
package fr.xebia.extras.selma.it.extend;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.Passenger;
import fr.xebia.extras.selma.beans.Proposal;
import fr.xebia.extras.selma.beans.ProposalDto;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by slemesle on 08/01/2017.
 */
@Compile(withClasses = {ExtendMapper.class, ExtendNamedMapper.class})
public class ExtendMapperIT extends IntegrationTestBase {

    @Test
    public void given_inheritMaps_on_immutable_mapping_should_use_inherited_maps_on_same_types_mapping_method(){
         // Given
        ExtendMapper mapper = Selma.mapper(ExtendMapper.class);

        Proposal proposal = new Proposal();
        proposal.setArrivalDate(new Date());
        proposal.setDepartureDate(new Date());
        proposal.setOrigin("Paris");
        proposal.setDestination("Lyon");
        proposal.setPrice(1000);
        proposal.setPassenger(new Passenger());
        proposal.getPassenger().setAge(26);
        proposal.getPassenger().setCard("YOUNG");
        Date passengerDate = new Date();
        proposal.getPassenger().setDate(passengerDate);

        ProposalDto proposalDto = mapper.asProposalDto(proposal);
        // When
        Proposal res = mapper.asProposal(proposalDto);

        // Then
        Assert.assertEquals(26, res.getPassenger().getAge());
        Assert.assertEquals("YOUNG", res.getPassenger().getCard());
        Assert.assertEquals(passengerDate, res.getPassenger().getDate());
    }

    @Test
    public void given_inheritMaps_on_update_source_mapping_should_use_inherited_maps_on_same_types_mapping_method() {
         // Given
        ExtendMapper mapper = Selma.mapper(ExtendMapper.class);

        Proposal proposal = new Proposal();
        proposal.setArrivalDate(new Date());
        proposal.setDepartureDate(new Date());
        proposal.setOrigin("Paris");
        proposal.setDestination("Lyon");
        proposal.setPrice(1000);
        proposal.setPassenger(new Passenger());
        proposal.getPassenger().setAge(26);
        proposal.getPassenger().setCard("YOUNG");
        Date passengerDate = new Date();
        proposal.getPassenger().setDate(passengerDate);

        ProposalDto proposalDto = mapper.asProposalDto(proposal);

        // When
        Proposal res = mapper.asProposal(proposalDto, proposal);

        // Then
        Assert.assertTrue(res == proposal);
        Assert.assertEquals(26, res.getPassenger().getAge());
        Assert.assertEquals("YOUNG", res.getPassenger().getCard());
        Assert.assertEquals(passengerDate, res.getPassenger().getDate());
    }

    @Test
    public void given_inheritMaps_with_method_name_on_mapping_should_use_inherited_maps_on_same_types_and_name_mapping_method() {
         // Given
        ExtendNamedMapper mapper = Selma.mapper(ExtendNamedMapper.class);

        Proposal proposal = new Proposal();
        proposal.setArrivalDate(new Date());
        proposal.setDepartureDate(new Date());
        proposal.setOrigin("Paris");
        proposal.setDestination("Lyon");
        proposal.setPrice(1000);
        proposal.setPassenger(new Passenger());
        proposal.getPassenger().setAge(26);
        proposal.getPassenger().setCard("YOUNG");
        Date passengerDate = new Date();
        proposal.getPassenger().setDate(passengerDate);

        ProposalDto proposalDto = mapper.asProposalDto(proposal);

        // When
        Proposal res = mapper.asProposal(proposalDto);

        // Then
        Assert.assertNull(res.getPassenger());
    }

}
