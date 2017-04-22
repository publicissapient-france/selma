package fr.xebia.extras.selma.it;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.Home;
import fr.xebia.extras.selma.beans.HomeDto;
import fr.xebia.extras.selma.beans.Phone;
import fr.xebia.extras.selma.it.mappers.HomeMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;

/**
 * @author FaniloRandria
 */
@Compile(withClasses = HomeMapper.class)
public class HomeMapperIT extends IntegrationTestBase {

    public static final String NUMBER_PHONE = "06-11-22-33-44";

    @Test
    public void beanMapperTest() throws Exception {
        HomeMapper mapper = Selma.getMapper(HomeMapper.class);
        Phone phone = new Phone(NUMBER_PHONE);
        Home home = new Home(phone);
        HomeDto homeDto = mapper.toDto(home);
        assertThat(homeDto.getPhone()).isEqualTo(NUMBER_PHONE);
    }

}
