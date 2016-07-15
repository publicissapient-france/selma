package fr.xebia.extras.selma.it.bug;

import fr.xebia.extras.selma.it.bug.beans.PersonMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

@Compile(withClasses = {}, withPackage = {"fr.xebia.extras.selma.it.bug.beans"}, shouldFail = true)
public class ConflictingClassAndFieldNameInCustomFieldIT extends IntegrationTestBase {

    @Test
    public void compilation_should_fail_on_custom_fields() throws Exception {

        assertCompilationError(PersonMapper.class, "PersonDto asDto(Person person);",
                "Mapping custom field city from source bean fr.xebia.extras.selma.it.bug.beans.Address, setter for " +
                        "field addresscity is missing in destination bean fr.xebia.extras.selma.it.bug.beans.AddressDto !");

    }

}
