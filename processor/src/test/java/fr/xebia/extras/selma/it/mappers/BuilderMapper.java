package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.BuilderIn;
import fr.xebia.extras.selma.beans.BuilderOut;

/**
 *
 */
@Mapper
public interface BuilderMapper {
    BuilderOut asBuilderOut(BuilderIn in);
}
