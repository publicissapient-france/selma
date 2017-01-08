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
@Compile(withClasses = {FailingExtendMapper.class}, shouldFail = true)
public class FailingExtendMapperIT extends IntegrationTestBase {

    @Test
    public void given_inheritMaps_on_mapping_with_multiple_maps_method_should_fail(){

    }

}
