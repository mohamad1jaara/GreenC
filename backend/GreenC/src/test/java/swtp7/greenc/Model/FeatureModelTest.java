package swtp7.greenc.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the FeatureModel class
 */
@SpringBootTest
public class FeatureModelTest extends ModelTestBaseSoftwareSystem1 {

    /**
     * Test for the checkValidity(Configuration) method of the influence model class with a valid configuration.
     */
    @Test
    public void testValidConfigurations(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem();
        Configuration validConfiguration = super.getValidConfiguration();
        Configuration maxConfiguration = super.getMaxConfiguration();
        Configuration minConfiguration = super.getMinConfiguration();
        assertEquals(true, softwareSystem.getGeneralModel().getFeatureModel().checkValidity(validConfiguration));
        assertEquals(true, softwareSystem.getGeneralModel().getFeatureModel().checkValidity(maxConfiguration));
        assertEquals(true, softwareSystem.getGeneralModel().getFeatureModel().checkValidity(minConfiguration));
    }

    /**
     * Test for the checkValidity(Configuration) method of the influence model class with an invalid configuration.
     */
    @Test
    public void testInvalidConfiguration(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem();
        Configuration invalidConfiguration = super.getInvalidConfiguration();
        assertEquals(false, softwareSystem.getGeneralModel().getFeatureModel().checkValidity(invalidConfiguration));
    }

    /**
     * Test for the findValidAlternative(Configuration configuration) method. Creates 100 random configurations. If a configuration
     * isn't valid (which is the case in more then 99%) the FeatureModel finds a valid alternative. It then gets checked whether
     * this alternative is valid.
     */
    @Test
    public void findValidAlternativeForRandomConfsTest(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem();
        for(int i = 0; i <= 100; i++){
            Configuration randomConfiguration = super.getRandomConfiguration();
            if(!softwareSystem.getGeneralModel().getFeatureModel().checkValidity(randomConfiguration)){
                assertEquals(softwareSystem.getGeneralModel().getFeatureModel().checkValidity
                        (softwareSystem.getGeneralModel().getFeatureModel().findValidAlternative(randomConfiguration)),true);
            }
        }
    }
}
