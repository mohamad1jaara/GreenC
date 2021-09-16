package swtp7.greenc.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GeneralModelTest extends ModelTestBaseSoftwareSystem1{

    /**
     * Test method for the evaluateConfiguration method of the GeneralModel class
     * Checks first the validity and then the calculated system values of the estimations
     *
     * part values tbc
     */
    @Test
    void evaluateConfigurations(){

        SoftwareSystem softwareSystem = super.getSoftwareSystem();
        Configuration validConfiguration = super.getValidConfiguration();
        Configuration MaxConfiguration = super.getMaxConfiguration();
        Configuration MinConfiguration = super.getMinConfiguration();
        Configuration invalidConfiguration = super.getInvalidConfiguration();

        assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(validConfiguration).getIsValid(), true);
        assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(MaxConfiguration).getIsValid(), true);
        assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(MinConfiguration).getIsValid(), true);
        assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(invalidConfiguration).getIsValid(), false);

        for(Property p: softwareSystem.getProperties()){

            assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(MaxConfiguration)
                    .getEstimation().getSystemValues().get(p).floatValue(), super.getExpectedMaxEstimation().getSystemValues()
                        .get(p).floatValue(), super.getDELTA().floatValue());

            assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(MinConfiguration)
                    .getEstimation().getSystemValues().get(p).floatValue(), super.getExpectedMinEstimation().getSystemValues()
                    .get(p).floatValue(), super.getDELTA().floatValue());
        }
    }

}
