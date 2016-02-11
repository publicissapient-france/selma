package fr.xebia.extras.selma.it.bug;

import java.util.ArrayList;
import java.util.List;

import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;
import fr.xebia.extras.selma.it.bug.beans.Address;
import fr.xebia.extras.selma.it.bug.beans.Person;
import fr.xebia.extras.selma.it.bug.beans.PersonDto;
import fr.xebia.extras.selma.it.bug.beans.PersonMapper;
import fr.xebia.extras.selma.it.mappers.MissingPropertyMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import fr.xebia.extras.selma.Selma;
import static org.junit.Assert.*;

@Compile(withClasses = {}, withPackage = {"fr.xebia.extras.selma.it.bug.beans"}, shouldFail = true)
public class ConflictingClassAndFieldNameInCustomFieldIT extends IntegrationTestBase {

    @Test
    public void compilation_should_fail_on_custom_fields() throws Exception {

        assertCompilationError(PersonMapper.class, "PersonDto asDto(Person person);",
                "Mapping custom field city from source bean fr.xebia.extras.selma.it.bug.beans.Address, setter for " +
                "field addresscity is missing in destination bean fr.xebia.extras.selma.it.bug.beans.AddressDto !");

        assertCompilationError(PersonMapper.class, "PersonDto asDto(Person person);",
                "Mapping custom field country from source bean fr.xebia.extras.selma.it.bug.beans.Address, setter " +
                        "for field addresscountry is missing");

        assertCompilationError(PersonMapper.class, "PersonDto asDto(Person person);",
                "Mapping custom field street from source bean fr.xebia.extras.selma.it.bug.beans.Address, setter " +
                        "for field addressstreet is missing");

        assertCompilationError(PersonMapper.class, "PersonDto asDto(Person person);",
                "Mapping custom field zipcode from source bean fr.xebia.extras.selma.it.bug.beans.Address, setter " +
                        "for field addresszipcode is missing");

        Assert.assertEquals(4, compilationErrorCount());
    }

}
