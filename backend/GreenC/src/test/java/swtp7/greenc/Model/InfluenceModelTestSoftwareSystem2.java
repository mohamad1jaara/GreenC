package swtp7.greenc.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class InfluenceModelTestSoftwareSystem2 extends ModelTestBaseSoftwareSystem2{

    /**
     * Test for the estimateConfiguration method of the InfluenceModel class
     *
     * So far only SystemValues are relevant, part values tbc
     */
    @Test
    public void estimateConfigurationSystemValuesTest(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem2();
        Configuration MaxConfiguration = super.getMaxConfiguration();
        Configuration MinConfiguration = super.getMinConfiguration();

        Estimation maxEstimation = softwareSystem.getGeneralModel().getInfluenceModel().estimateConfiguration(MaxConfiguration);
        Estimation minEstimation = softwareSystem.getGeneralModel().getInfluenceModel().estimateConfiguration(MinConfiguration);

        int property_count = 0;
        for(Property p: maxEstimation.getSystemValues().keySet()){
            assertEquals(maxEstimation.getSystemValues().get(p).floatValue(), super.getExpectedMaxEstimation().getSystemValues().get(p).floatValue(),
                    super.getDELTA().floatValue());
            property_count ++;
        }
        assertEquals(6, property_count);
        property_count = 0;
        for(Property p: minEstimation.getSystemValues().keySet()){
            assertEquals(minEstimation.getSystemValues().get(p).floatValue(), super.getExpectedMinEstimation().getSystemValues().get(p).floatValue(),
                    super.getDELTA().floatValue());
            property_count ++;
        }
        assertEquals(6, property_count);
    }
}
