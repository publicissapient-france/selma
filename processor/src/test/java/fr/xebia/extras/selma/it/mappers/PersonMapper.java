package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.Person;
import fr.xebia.extras.selma.beans.PersonDto;

/**
 * Created by dtente on 28/09/2016.
 */
@Mapper
public interface PersonMapper {

  PersonDto toDto(Person patient);

}
