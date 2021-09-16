package swtp7.greenc.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the FeatureModel class regarding the 7Zip SoftwareSystem
 */
@SpringBootTest
public class FeatureModelTestSoftwareSystem2 extends ModelTestBaseSoftwareSystem2 {

    /**
     * Test for the checkValidity(Configuration) method of the influence model class with a valid configuration.
     */
    @Test
    public void testValidConfiguration(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem2();
        Configuration validConfiguration = super.getValidConfiguration();
        assertEquals(true, softwareSystem.getGeneralModel().getFeatureModel().checkValidity(validConfiguration));
    }

    /**
     * Test for the checkValidity(Configuration) method of the influence model class with an invalid configuration.
     */
    @Test
    public void testInvalidConfiguration(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem2();
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
        SoftwareSystem softwareSystem = super.getSoftwareSystem2();
        for(int i = 0; i <= 100; i++){
            boolean numericOptionsAreValid = false;
            if((i % 2) == 0){
                numericOptionsAreValid = true;
            }
            Configuration randomConfiguration = super.getRandomConfiguration(numericOptionsAreValid);

            if(!softwareSystem.getGeneralModel().getFeatureModel().checkValidity(randomConfiguration)){
                assertEquals(softwareSystem.getGeneralModel().getFeatureModel().checkValidity
                        (softwareSystem.getGeneralModel().getFeatureModel().findValidAlternative(randomConfiguration)),true);
            }

            Configuration newConfiguration = softwareSystem.getGeneralModel().getFeatureModel().findValidAlternative(randomConfiguration);
        }
    }

    @Test
    public void findValidAlternativeTest(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem2();
        Configuration alternateConfiguration = softwareSystem.getGeneralModel().getFeatureModel().findValidAlternative(super.getInvalidConfigurationWithFixedNumericOptions());
        int count = 0;
        for (NumericOption numericOption: alternateConfiguration.getNumericOptionValues().keySet()){
            if (numericOption.getName().equals("Files")){
                assertEquals(10, alternateConfiguration.getNumericOptionValues().get(numericOption));
                count ++;
            }
            if (numericOption.getName().equals("x")){
                assertEquals(10, alternateConfiguration.getNumericOptionValues().get(numericOption));
                count ++;
            }
            if (numericOption.getName().equals("BlockSize")){
                assertEquals(8, alternateConfiguration.getNumericOptionValues().get(numericOption));
                count++;
            }
        }
        assertEquals(3, count);
    }
}
