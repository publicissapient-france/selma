package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.CityOut;

@Mapper(withIgnoreNullValue = true)
public interface NotNullMapper {
    CityOut mapCity(CityIn cityIn, CityOut out);
}
