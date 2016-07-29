package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.BuilderIn;
import fr.xebia.extras.selma.beans.BuilderOut;
import fr.xebia.extras.selma.it.mappers.BuilderMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
@Compile(withClasses = BuilderMapper.class)
public class BuilderMapperIT extends IntegrationTestBase {

    @Test
    public void builderMapper_should_map_properties() throws Exception {
        BuilderMapper mapper = Selma.getMapper(BuilderMapper.class);

        BuilderIn builderIn = new BuilderIn();
        builderIn.setIntVal(5);
        builderIn.setStr("a string");

        BuilderOut res = mapper.asBuilderOut(builderIn);

        Assert.assertNotNull(res);
        Assert.assertEquals(builderIn.getIntVal(), res.getIntVal());
        Assert.assertEquals(builderIn.getStr(), res.getStr());
    }
}
