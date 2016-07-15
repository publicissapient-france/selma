package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.IbanRest;
import fr.xebia.extras.selma.beans.Rib;

@Mapper(withCustomFields = {@Field({"IbanRest.codeBanque", "Rib.codeBanque"}), @Field({"IbanRest.codeBanqueDomiciliation", "Rib.codeBanqueCompensation"}),})
public interface IbanMapper {

    IbanRest convert(Rib rib);

    Rib convert(IbanRest iban);
}
