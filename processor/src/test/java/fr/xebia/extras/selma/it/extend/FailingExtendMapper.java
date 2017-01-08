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

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.InheritMaps;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import fr.xebia.extras.selma.beans.Proposal;
import fr.xebia.extras.selma.beans.ProposalDto;

/**
 * Created by slemesle on 05/08/2016.
 */
@Mapper
public interface FailingExtendMapper {

    @Maps(withCustomFields = {
            @Field({"Proposal.passenger.age", "ProposalDto.passengerAge"}), @Field({"passenger.card", "passengerCard"}),
            @Field({"Proposal.passenger.date", "ProposalDto.passengerDate"})
    }) ProposalDto asProposalDto(Proposal proposal);

    @Maps(withIgnoreFields = {"passengerAge", "passengerDate", "passenger", "passengerCard"})
    ProposalDto asReducedProposalDto(Proposal proposal);

    @InheritMaps
    Proposal asProposal(ProposalDto proposal);

    @InheritMaps
    Proposal asProposal(ProposalDto proposal, Proposal out);
}
