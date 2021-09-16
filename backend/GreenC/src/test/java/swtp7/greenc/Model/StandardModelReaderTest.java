package swtp7.greenc.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the StandardModelReader class regarding the SoftwareSystem1
 *
 */
@SpringBootTest
public class StandardModelReaderTest extends ModelTestBaseSoftwareSystem1 {

    public StandardModelReader getModelReader() throws IOException {

            File featureModelXml = new ClassPathResource("SoftwareSystems/HSQLDB/FeatureModel.xml").getFile();
            File featureModelDimacs = new ClassPathResource("SoftwareSystems/HSQLDB/FeatureModel.dimacs").getFile();
            File influenceModelCsv = new ClassPathResource("SoftwareSystems/HSQLDB/model.csv").getFile();
            StandardModelReader modelReader = new StandardModelReader(featureModelXml, featureModelDimacs, influenceModelCsv);
            return modelReader;
    }

    /**
     * Test method for the getOption method
     * Unfinished due to some questions: Shouldn't the memory_tables / cached_tables options be optional?
     */
    @Test
    public void getBinaryOptionsTest(){
        List<BinaryOption> options = new ArrayList<>();
        try {
            options = getModelReader().getBinaryOptions();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<BinaryOption> expectedOptions = new ArrayList<>();

        BinaryOption root = new BinaryOption("root", null, false);
        BinaryOption table_type = new BinaryOption("table_type", root, false);
        BinaryOption memory_tables = new BinaryOption("memory_tables", table_type, false);
        BinaryOption cached_tables = new BinaryOption("cached_tables", table_type, false);
        expectedOptions.add(root);
        expectedOptions.add(table_type);
        expectedOptions.add(memory_tables);
        expectedOptions.add(cached_tables);

        int countOptions = 0;

        for(BinaryOption option: options){
            for(BinaryOption expectedOption: expectedOptions){
                if(option.equals(expectedOption)){
                    countOptions = countOptions + 1;
                    assertEquals(expectedOption.getName(), option.getName());
                    if(!(option.getParent()==null)) {
                        assertEquals(expectedOption.getParent().getName(), option.getParent().getName());
                    }
                    assertEquals(expectedOption.isOptional(), option.isOptional());
                }
            }
        }
        assertEquals(expectedOptions.size(), countOptions);
    }

    /**
     * Test for the getInfluences() method. Compares the influences from the partValues of the ModelTestBase.getExpectedMaxEstimation
     * with the ModelReader ones. Test is similar to the InfluenceModelTest.estimateConfigurationPartValuesTest() due to the effort it takes to
     * look up influences in the model.csv
     */
    @Test
    public void getInfluencesTest(){

        List<Influence> influences = new ArrayList<>();
        try {
            influences = getModelReader().getInfluences();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Influence> expectedInfluences = super.getExpectedMaxEstimation().getPartValues();

        int partValueCount = 0;

        for(Influence influence: influences){
            for(Influence expectedInfluence: expectedInfluences){
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
        assertEquals(partValueCount, expectedInfluences.size());
    }

    /**
     * Test for the getProperties() method
     */
    @Test
    public void getPropertiesTest(){
        List<Property> properties = new ArrayList<>();
        try {
            properties = getModelReader().getProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Property> expectedProperties = new ArrayList<>();
        expectedProperties.add(new Property("energy_consumption", "Ws","<"));
        expectedProperties.add(new Property("run_time", "s","<"));
        expectedProperties.add(new Property("cpu_load", "%", "<"));

        assertEquals(expectedProperties.size(), properties.size());

        for(Property expectedProperty: expectedProperties){
            boolean contains = false;
            for(Property property: properties) {
                if (property.getName().equals(expectedProperty.getName()) && property.getUnit().equals(expectedProperty.getUnit())) {
                    contains = true;
                    break;
                }
            }
            assertTrue(contains);
        }
    }

    /**
     * Test for the getClauses() method
     */
    @Test
    public void getClausesTest(){
        List<Clause> clauses = new ArrayList<>();
        try {
            clauses = getModelReader().getClauses();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Clause> expectedClauses = new ArrayList<>();

        List<BinaryOption> positiveOptions = new ArrayList<>();
        List<BinaryOption> negativeOptions = new ArrayList<>();

        positiveOptions.add(new BinaryOption("root"));

        Clause clause = new Clause();
        clause.setPositiveLiterals(positiveOptions);
        clause.setNegativeLiterals(negativeOptions);
        expectedClauses.add(clause);

        positiveOptions = new ArrayList<>();
        negativeOptions = new ArrayList<>();

        positiveOptions.add(new BinaryOption("transaction_control"));

        clause = new Clause();
        clause.setPositiveLiterals(positiveOptions);
        clause.setNegativeLiterals(negativeOptions);
        expectedClauses.add(clause);

        positiveOptions = new ArrayList<>();
        negativeOptions = new ArrayList<>();

        positiveOptions.add(new BinaryOption("crypt_blowfish"));
        positiveOptions.add(new BinaryOption("crypt_aes"));
        negativeOptions.add(new BinaryOption("encryption"));

        clause = new Clause();
        clause.setPositiveLiterals(positiveOptions);
        clause.setNegativeLiterals(negativeOptions);
        expectedClauses.add(clause);

        positiveOptions = new ArrayList<>();
        negativeOptions = new ArrayList<>();

        negativeOptions.add(new BinaryOption("cached_tables"));
        negativeOptions.add(new BinaryOption("memory_tables"));

        clause = new Clause();
        clause.setPositiveLiterals(positiveOptions);
        clause.setNegativeLiterals(negativeOptions);
        expectedClauses.add(clause);
        int clauseCount = 0;
        for(Clause c: clauses){
            for(Clause expectedC: expectedClauses){
                boolean isContained = false;
                if(c.getPositiveLiterals().size()==expectedC.getPositiveLiterals().size() &&
                c.getNegativeLiterals().size()==expectedC.getNegativeLiterals().size()){
                    isContained = true;
                    for(Option option: c.getPositiveLiterals()){
                        if(!expectedC.getPositiveLiterals().contains(option)){
                            isContained = false;
                        }
                    }
                    for(Option option: c.getNegativeLiterals()){
                        if(!expectedC.getNegativeLiterals().contains(option)){
                            isContained = false;
                        }
                    }
                    if(isContained){
                        clauseCount = clauseCount + 1;
                    }
                }
            }
        }
        assertEquals(expectedClauses.size(), clauseCount);
    }
}

