package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.IbanRest;
import fr.xebia.extras.selma.beans.Rib;
import fr.xebia.extras.selma.it.mappers.IbanMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

@Compile(withClasses = {IbanMapper.class})
public class IbanMapperIT extends IntegrationTestBase {

    @Test
    public void startWith_Iban_to_Rib_Test() {
        final IbanMapper mapper = Selma.mapper(IbanMapper.class);

        IbanRest ibanRest = new IbanRest(12, 25);

        Rib rib = mapper.convert(ibanRest);

        Assert.assertEquals(ibanRest.getCodeBanque(), rib.getCodeBanque());
        Assert.assertEquals(ibanRest.getCodeBanqueDomiciliation(), rib.getCodeBanqueCompensation());
    }

    @Test
    public void startWith_Rib_to_Iban_Test() {
        final IbanMapper mapper = Selma.mapper(IbanMapper.class);

        Rib rib = new Rib(12, 25);

        IbanRest ibanRest = mapper.convert(rib);

        Assert.assertEquals(ibanRest.getCodeBanque(), rib.getCodeBanque());
        Assert.assertEquals(ibanRest.getCodeBanqueDomiciliation(), rib.getCodeBanqueCompensation());
    }
}
