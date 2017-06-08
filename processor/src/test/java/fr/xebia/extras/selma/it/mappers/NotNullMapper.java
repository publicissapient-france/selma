package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.CityOut;
import fr.xebia.extras.selma.beans.Library;
import fr.xebia.extras.selma.beans.LibraryDTO;

@Mapper(withIgnoreNullValue = true)
public interface NotNullMapper {
    CityOut mapCity(CityIn cityIn, CityOut out);

    @Maps(withIgnoreMissing = IgnoreMissing.DESTINATION)
    LibraryDTO mapLibrary(Library in, LibraryDTO out);

    StringContainer mapStringContainer(StringContainer in, StringContainer out);

    class StringContainer {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
