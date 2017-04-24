package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import fr.xebia.extras.selma.beans.Home;
import fr.xebia.extras.selma.beans.HomeDto;

/**
 * @author FaniloRandria
 */
@Mapper
public interface HomeMapper {

    @Maps(withCustomFields = {
    		@Field(value = {"phone", "phoneHome.phoneNumber"})
    }) HomeDto toDto (Home in);
    
}
