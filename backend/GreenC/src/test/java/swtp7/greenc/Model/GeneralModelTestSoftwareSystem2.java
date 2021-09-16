package swtp7.greenc.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GeneralModelTestSoftwareSystem2 extends ModelTestBaseSoftwareSystem2{

    /**
     * Test for the findValidAlternative method of the featureModel Class; the improved Min and the improved MaxConfigurations
     * have been calculated by hand.
     */
    @Test
    void testFindImprovedAlternative(){
        Configuration minConfiguration = super.getMinConfiguration();

        List<BinaryOption> improvedMinOptions = new ArrayList<>();
        improvedMinOptions.add(new BinaryOption("root"));
        improvedMinOptions.add(new BinaryOption("CompressionMethod"));
        improvedMinOptions.add(new BinaryOption("PPMd"));

        Configuration improvedMinConfigurationExpected = new Configuration(improvedMinOptions, minConfiguration.getNumericOptionValues());
        ImprovementRequest improvementRequest = new ImprovementRequest(new Property("cpu_load_fixed"), minConfiguration);
        ImprovementRequest improvementRequest2 = super.getSoftwareSystem2().getGeneralModel().findImprovedAlternative(improvementRequest);
        Configuration improvedMinConfigurationReal = improvementRequest2.getImprovedConfiguration();
        int count = 0;
        for (NumericOption numericOption: improvedMinConfigurationReal.getNumericOptionValues().keySet()){
            for(NumericOption numericOption1:improvedMinConfigurationExpected.getNumericOptionValues().keySet()){
                if(numericOption.getName().equals(numericOption1.getName())){
                    count ++;
                }
            }
        }

        assertEquals(3, count);

        Configuration maxConfiguration = super.getMaxConfigurationWithMinNumericValues();

        List<BinaryOption> improvedMaxOptions = new ArrayList<>();

        improvedMaxOptions.add(new BinaryOption("root"));
        improvedMaxOptions.add(new BinaryOption("CompressionMethod"));
        improvedMaxOptions.add(new BinaryOption("PPMd"));
        improvedMaxOptions.add(new BinaryOption("filterOff"));
        improvedMaxOptions.add(new BinaryOption("HeaderCompressionOff"));
        improvedMaxOptions.add(new BinaryOption("mtOff"));
        improvedMaxOptions.add(new BinaryOption("tmOff"));

        Configuration improvedMaxConfigurationExpected = new Configuration(improvedMaxOptions, maxConfiguration.getNumericOptionValues());
        ImprovementRequest improvementRequestMax = new ImprovementRequest(new Property("cpu_load_fixed"), maxConfiguration);
        ImprovementRequest improvementRequestMax2 = super.getSoftwareSystem2().getGeneralModel().findImprovedAlternative(improvementRequestMax);
        Configuration improvedMaxConfigurationReal = improvementRequestMax2.getImprovedConfiguration();

        count = 0;
        for (NumericOption numericOption: improvedMaxConfigurationReal.getNumericOptionValues().keySet()){
            for(NumericOption numericOption1:improvedMaxConfigurationExpected.getNumericOptionValues().keySet()){
                if(numericOption.getName().equals(numericOption1.getName())){
                    count ++;
                }
            }
        }
        assertEquals(3, count);
    }

    /**
     * Test for the evaluateConfiguration method of the featureModel class
     */
    @Test
    void evaluateConfigurations(){

        SoftwareSystem softwareSystem = super.getSoftwareSystem2();
        Configuration validConfiguration = super.getValidConfiguration();
        Configuration MaxConfiguration = super.getMaxConfiguration();
        Configuration MinConfiguration = super.getMinConfiguration();
        Configuration invalidConfiguration = super.getInvalidConfiguration();

        assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(validConfiguration).getIsValid(), true);
        assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(MaxConfiguration).getIsValid(), true);
        assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(MinConfiguration).getIsValid(), true);
        assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(invalidConfiguration).getIsValid(), false);

        for(Property p: softwareSystem.getProperties()) {
            assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(MaxConfiguration)
                    .getEstimation().getSystemValues().get(p).floatValue(), super.getExpectedMaxEstimation().getSystemValues()
                    .get(p).floatValue(), super.getDELTA().floatValue());
            assertEquals(softwareSystem.getGeneralModel().evaluateConfiguration(MinConfiguration)
                    .getEstimation().getSystemValues().get(p).floatValue(), super.getExpectedMinEstimation().getSystemValues()
                    .get(p).floatValue(), super.getDELTA().floatValue());
        }
    }
}
