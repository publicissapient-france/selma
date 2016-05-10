package fr.xebia.extras.selma.it.custom.mapper;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by v.koroteev on 10.05.2016.
 */

@Compile(withClasses = {AbstractMapperWithInterceptor.class})
public class AbstractMapperWithInterceptorIT extends IntegrationTestBase {

    @Test
    public void should_map_bean_with_interceptor_in_abstract_mapper() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        AbstractMapperWithInterceptor mapper = Selma.getMapper(AbstractMapperWithInterceptor.class);

        PersonIn personIn = new PersonIn();
        personIn.setAddress(new AddressIn());
        personIn.getAddress().setCity(new CityIn());
        personIn.getAddress().getCity().setCapital(true);
        personIn.getAddress().getCity().setName("Paris");
        personIn.getAddress().getCity().setPopulation(3 * 1000 * 1000);

        personIn.getAddress().setPrincipal(true);
        personIn.getAddress().setNumber(55);
        personIn.getAddress().setStreet("rue de la truanderie");

        PersonOut res = mapper.asPersonOut(personIn);

        Assert.assertNotNull(res);

        Assert.assertEquals(personIn.getAddress().getStreet(), res.getAddress().getStreet());
        Assert.assertEquals(personIn.getAddress().getNumber() +  AbstractMapperWithInterceptor.NUMBER_INCREMENT,
                res.getAddress().getNumber());                                               // interceptor here
        Assert.assertEquals(personIn.getAddress().getExtras(), res.getAddress().getExtras());

        Assert.assertEquals(personIn.getAddress().getCity().getName(), res.getAddress().getCity().getName());
        Assert.assertEquals(personIn.getAddress().getCity().getPopulation(), res.getAddress().getCity().getPopulation());
        Assert.assertEquals(personIn.getAddress().getCity().isCapital(), res.getAddress().getCity().isCapital());
    }
}
