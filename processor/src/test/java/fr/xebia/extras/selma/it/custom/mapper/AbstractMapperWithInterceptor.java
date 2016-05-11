package fr.xebia.extras.selma.it.custom.mapper;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;

/**
 * Created by v.koroteev on 10.05.2016.
 */
@Mapper(withIgnoreFields = {"fr.xebia.extras.selma.beans.PersonIn.male", "fr.xebia.extras.selma.beans.PersonOut.biography"})
public abstract class AbstractMapperWithInterceptor {

    public static final int NUMBER_INCREMENT = 10000;

    public abstract PersonOut asPersonOut(PersonIn in);

    public void interceptor(PersonIn in, PersonOut out) {
        out.getAddress().setNumber(in.getAddress().getNumber() + NUMBER_INCREMENT);
    }
}
