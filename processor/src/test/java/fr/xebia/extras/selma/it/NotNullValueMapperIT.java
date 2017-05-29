package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.beans.*;
import fr.xebia.extras.selma.it.mappers.NotNullMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

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

    @Test
    public void should_not_map_null_collections(){
        NotNullMapper mapper = builder(NotNullMapper.class).build();
        Library library = new Library();
        library.setBooks(null);
        library.setName("toto");
        LibraryDTO res = new LibraryDTO();
        res.setBooks(new ArrayList<BookDTO>());
        res = mapper.mapLibrary(library, res);

        Assert.assertNotNull(res.getBooks());
    }

}
