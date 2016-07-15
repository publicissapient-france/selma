package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.CityOut;
import fr.xebia.extras.selma.it.mappers.NotNullMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import static fr.xebia.extras.selma.Selma.builder;

@Compile(withClasses = {
        NotNullMapper.class
})
public class NotNullValueMapperIT extends IntegrationTestBase {

    @Test
    public void test_should_not_map__null_value() {
        NotNullMapper mapper = builder(NotNullMapper.class).build();
        CityOut cityOut = new CityOut();
        cityOut.setName("Name");
        cityOut = mapper.mapCity(new CityIn(), cityOut);
        Assert.assertEquals(cityOut.getName(), "Name");
    }

    @Test
    public void test_should_map_not_null_value() {
        NotNullMapper mapper = builder(NotNullMapper.class).build();
        CityOut cityOut = new CityOut();
        cityOut.setName("Name");
        CityIn cityIn = new CityIn();
        cityIn.setName("City");
        cityOut = mapper.mapCity(cityIn, cityOut);
        Assert.assertEquals(cityOut.getName(), "City");
    }
}
