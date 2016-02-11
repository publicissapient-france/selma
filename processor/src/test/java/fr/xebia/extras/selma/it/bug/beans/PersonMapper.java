package fr.xebia.extras.selma.it.bug.beans;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;

@Mapper(
    withCustomFields = {
        @Field({"address.street","addressStreet"}),
        @Field({"address.zipCode","addressZipCode"}),
        @Field({"address.city","addressCity"}),
        @Field({"address.country","addressCountry"})
    }
)
public interface PersonMapper
{
    Person asModel(PersonDto personDto);
    
    PersonDto asDto(Person person);
}
