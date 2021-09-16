package swtp7.greenc.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class InfluenceModelTest extends ModelTestBaseSoftwareSystem1{

    /**
     * Test for the estimateConfiguration method of the InfluenceModel class
     */
    @Test
    public void estimateConfigurationSystemValuesTest(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem();
        Configuration MaxConfiguration = super.getMaxConfiguration();
        Configuration MinConfiguration = super.getMinConfiguration();

        Estimation maxEstimation = softwareSystem.getGeneralModel().getInfluenceModel().estimateConfiguration(MaxConfiguration);
        Estimation minEstimation = softwareSystem.getGeneralModel().getInfluenceModel().estimateConfiguration(MinConfiguration);

        for(Property p: maxEstimation.getSystemValues().keySet()){
            assertEquals(maxEstimation.getSystemValues().get(p).floatValue(), super.getExpectedMaxEstimation().getSystemValues().get(p).floatValue(),
                    super.getDELTA().floatValue());

        }
        for(Property p: minEstimation.getSystemValues().keySet()){
            assertEquals(minEstimation.getSystemValues().get(p).floatValue(), super.getExpectedMinEstimation().getSystemValues().get(p).floatValue(),
                    super.getDELTA().floatValue());
        }
    }

    /**
     * Second test for the estimateConfiguration method. This test checks if some of the partValues of the MaxConfiguration are correct.
     */
    @Test
    public void estimateConfigurationPartValuesTest(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem();
        Configuration MaxConfiguration = super.getMaxConfiguration();

        Estimation maxEstimation = softwareSystem.getGeneralModel().getInfluenceModel().estimateConfiguration(MaxConfiguration);
        Estimation expectedMaxEstimation = super.getExpectedMaxEstimation();
        int partValueCount = 0;

        for(Influence influence: maxEstimation.getPartValues()){
            for(Influence expectedInfluence: expectedMaxEstimation.getPartValues()){
                if(influence.getBinaryOptions().size()==expectedInfluence.getBinaryOptions().size()){
                    boolean optionsAreEqual = true;
                    for(Option option: influence.getBinaryOptions()) {
                        if (!(expectedInfluence.getBinaryOptions().contains(option))) {
                            optionsAreEqual = false;
                        }
                    }
                    if(optionsAreEqual) {
                        partValueCount = partValueCount + 1;
                        for (Property property : influence.getValues().keySet()) {
                            assertEquals(influence.getValues().get(property).floatValue(), expectedInfluence.getValues()
                                    .get(property).floatValue(), super.getDELTA().floatValue());
                        }
                    }
                }
            }
        }
        assertEquals(partValueCount, expectedMaxEstimation.getPartValues().size());
    }
}
