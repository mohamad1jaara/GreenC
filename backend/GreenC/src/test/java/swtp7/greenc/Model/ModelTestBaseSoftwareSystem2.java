package swtp7.greenc.Model;

import swtp7.greenc.Service.GreenConfiguratorConfiguration;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Base class for the backend tests regarding SoftwareSystem2 (7zip)
 */
public abstract class ModelTestBaseSoftwareSystem2 {

    /**
     * Delta value for assertEqual with to BigDecimals
     */
    private static final BigDecimal DELTA = new BigDecimal(1e-15);

    /**
     * Method to create a SoftwareSystem2 instance based on the 3 given xml, dimacs and csv files
     *
     * @return Softwaresystem
     */
    public SoftwareSystem getSoftwareSystem2() {

        GreenConfiguratorConfiguration greenConfiguratorConfiguration = new GreenConfiguratorConfiguration();
        SoftwareSystem softwareSystem = greenConfiguratorConfiguration.readSoftwareSystemsFromDisk().get(1);
        return softwareSystem;
    }

    /**
     * Method to create a fully randomized new configuration instance. Every option of the SoftwareSystem 7Zip has a 50/50
     * chance of being activated in the configuration instance. The boolean indicates if the numericOptionValues of the configuration
     * are valid.
     *
     * @return Configuration a randomly created configuration instance
     */
    public Configuration getRandomConfiguration(boolean numericOptionsAreValid) {
        SoftwareSystem softwaresystem = getSoftwareSystem2();
        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();

        for (BinaryOption binaryOption : softwaresystem.getBinaryOptions()) {
            if (Math.random() < 0.5) {
                activatedOptions.add(binaryOption);
            }
        }
        Map<NumericOption, Integer> numericOptionValues = getNumericOptions(numericOptionsAreValid);
        return new Configuration(activatedOptions, numericOptionValues);
    }

    /**
     * Help method to create a Map of all the NumericOptions of the 7Zip SoftwareSystem with random values selected from
     * an array
     *
     * @param isValid if set to true, the value will be set to a valid value
     * @return Map<NumericOption, Integer>
     */
    public Map<NumericOption, Integer> getNumericOptions(boolean isValid) {
        SoftwareSystem softwareSystem = getSoftwareSystem2();
        Random random = new Random();
        int[] valid = new int[10];
        int[] invalid = new int[10];
        Map<NumericOption, Integer> numericOptionValues = new HashMap<>();
        for (NumericOption numericOption : softwareSystem.getNumericOptions()) {
            if (numericOption.getStepFunction().getName().equals("x + 2")) {
                valid = new int[]{0, 2, 4, 6, 8, 10, 2, 4, 6, 8};
                invalid = new int[]{5, 9, 63, 81, 105, 201, 1001, 3099, 5000774, 79};
            }
            if (numericOption.getStepFunction().getName().equals("BlockSize * 2")) {
                valid = new int[]{1, 4, 8, 16, 128, 256, 512, 2048, 1024, 4096};
                invalid = new int[]{5, 9, 63, 81, 105, 201, 1001, 3099, 5000773, 7};
            }
            if (numericOption.getStepFunction().getName().equals("Files + 10")) {
                valid = new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90};
                invalid = new int[]{5, 9, 63, 81, 105, 201, 1001, 3099, 5000775, 7};
            }
            int randomIndex = random.nextInt(10);
            if (isValid) {
                numericOptionValues.put(numericOption, valid[randomIndex]);
            } else {
                numericOptionValues.put(numericOption, invalid[randomIndex]);
            }
        }
        return numericOptionValues;
    }

    public Configuration getValidConfiguration() {
        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("CompressionMethod"));
        activatedOptions.add(new BinaryOption("Deflate"));
        return new Configuration(activatedOptions, getNumericOptions(true));
    }

    public Configuration getInvalidConfiguration() {
        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("CompressionMethod"));
        activatedOptions.add(new BinaryOption("LZMA"));
        activatedOptions.add(new BinaryOption("LZMA2"));
        return new Configuration(activatedOptions, getNumericOptions(false));
    }

    public Configuration getInvalidConfigurationWithFixedNumericOptions(){
        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("CompressionMethod"));
        activatedOptions.add(new BinaryOption("LZMA"));
        activatedOptions.add(new BinaryOption("LZMA2"));

        Map<NumericOption, Integer> numericOptionValues = new HashMap<>();
        numericOptionValues.put(new NumericOption("Files"), 9);
        numericOptionValues.put(new NumericOption("x"), 11);
        numericOptionValues.put(new NumericOption("BlockSize"), 9);
        return new Configuration(activatedOptions, numericOptionValues);
    }

    /**
     * Creates a valid configuration with 13 activated options. Every configuration with more activated options
     * wont be valid for our "SoftwareSystem1"
     * The NumericOptions will be set to the maximum values
     *
     * @return the configuration with maximum activated options
     */
    public Configuration getMaxConfiguration() {
        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("CompressionMethod"));
        activatedOptions.add(new BinaryOption("LZMA"));
        activatedOptions.add(new BinaryOption("mtOff"));
        activatedOptions.add(new BinaryOption("filterOff"));
        activatedOptions.add(new BinaryOption("HeaderCompressionOff"));
        activatedOptions.add(new BinaryOption("tmOff"));
        Map<NumericOption, Integer> numericOptionValues = new HashMap<>();
        int count = 0;
        for(NumericOption numericOption: getSoftwareSystem2().getNumericOptions()){
            if(numericOption.getName().equals("Files")){
                numericOptionValues.put(new NumericOption("Files"),100);
                count++;
            }
            if(numericOption.getName().equals("BlockSize")){
                numericOptionValues.put(new NumericOption("BlockSize"),4096);
                count++;
            }
            if(numericOption.getName().equals("x")){
                numericOptionValues.put(new NumericOption("x"),10);
                count++;
            }
        }
        assertEquals(3, count);
        return new Configuration(activatedOptions, numericOptionValues);
    }

    /**
     * Creates a valid configuration with 13 activated options. Every configuration with more activated options
     * wont be valid for our "SoftwareSystem1"
     * The NumericOptions are set to the min valid values
     *
     * @return the configuration with maximum activated options
     */
    public Configuration getMaxConfigurationWithMinNumericValues() {
        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("CompressionMethod"));
        activatedOptions.add(new BinaryOption("LZMA"));
        activatedOptions.add(new BinaryOption("mtOff"));
        activatedOptions.add(new BinaryOption("filterOff"));
        activatedOptions.add(new BinaryOption("HeaderCompressionOff"));
        activatedOptions.add(new BinaryOption("tmOff"));
        Map<NumericOption, Integer> numericOptionValues = new HashMap<>();
        int count = 0;
        for(NumericOption numericOption: getSoftwareSystem2().getNumericOptions()){
            if(numericOption.getName().equals("Files")){
                numericOptionValues.put(new NumericOption("Files"),0);
                count++;
            }
            if(numericOption.getName().equals("BlockSize")){
                numericOptionValues.put(new NumericOption("BlockSize"),1);
                count++;
            }
            if(numericOption.getName().equals("x")){
                numericOptionValues.put(new NumericOption("x"),0);
                count++;
            }
        }
        assertEquals(3, count);
        return new Configuration(activatedOptions, numericOptionValues);
    }

    /**
     * The estimation belonging to the MaxConfiguration above, information got extracted directly from the model2.csv
     *
     * @return maxEstimation
     */
    public Estimation getExpectedMaxEstimation(){

        BigDecimal cpu_load_fixed_bd = new BigDecimal(2.22695699444537 + -0.0010751214007876 * 100 +
                4096 * -0.0000190766646872552 + 0.956230060425901 * 10);
        BigDecimal cpu_load_bm_bd = new BigDecimal(35.8601227764242 + -7.52464736466063
                + -5.08808224768072E-015 + 0.0281979642370932 * 100 + 0.000355076261559318 * 4096
                + -0.390140159376969 * 10);
        BigDecimal energy_consumption_fixed_bd = new BigDecimal(8860.4003024894 + -0.295758674543274 * 100
        + -0.00433864437396869 * 4096 + 201.757161649164 * 10);
        BigDecimal energy_consumption_bm_bd = new BigDecimal(1211.33207934644 + -0.00930713072932593 * 4096 +
                -0.935849506649063 * 100 + 494.843869301189 * 10);
        BigDecimal compression_size_bd = new BigDecimal(71113559.9036727 + -74296.0656191023 * 100 +
                -1033.48475302018 * 4096 + -3233582.18419802 * 10);
        BigDecimal run_time_bd = new BigDecimal(33714.9482813344 + 5912.60856368295 + -0.24881685589545 * 4096
        + 0.000000000067298367862657 + -32.1659354344769 * 100 + 14666.9647570211 * 10);


        Map<Property, BigDecimal> maxSystemValues = new HashMap<>();
        Property cpu_load_fixed = new Property("cpu_load_fixed", "%", "<");
        Property cpu_load_bm = new Property("cpu_load_bm", "%", "<");
        Property energy_consumption_fixed = new Property("energy_consumption_fixed", "Ws","<");
        Property energy_consumption_bm = new Property("energy_consumption_bm", "Ws","<");
        Property compression_size = new Property("compression_size", "B","<");
        Property run_time = new Property("run_time", "ms","<");
        maxSystemValues.put(cpu_load_fixed, cpu_load_fixed_bd);
        maxSystemValues.put(cpu_load_bm, cpu_load_bm_bd);
        maxSystemValues.put(energy_consumption_fixed, energy_consumption_fixed_bd);
        maxSystemValues.put(energy_consumption_bm, energy_consumption_bm_bd);
        maxSystemValues.put(compression_size, compression_size_bd);
        maxSystemValues.put(run_time, run_time_bd);

        Estimation minEstimation = new Estimation(maxSystemValues, null);
        return minEstimation;
    }

    /**
     * Creates a valid configuration with 5 activated options. Every configuration with less activated options
     * wont be valid for our "SoftwareSystem1"
     *
     * @return the configuration with minimum activated options
     */
    public Configuration getMinConfiguration() {
        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("CompressionMethod"));
        activatedOptions.add(new BinaryOption("LZMA"));
        Map<NumericOption, Integer> numericOptionValues = new HashMap<>();
        int count = 0;
        for(NumericOption numericOption: getSoftwareSystem2().getNumericOptions()){
            if(numericOption.getName().equals("Files")){
                numericOptionValues.put(new NumericOption("Files"),0);
                count++;
            }
            if(numericOption.getName().equals("BlockSize")){
                numericOptionValues.put(new NumericOption("BlockSize"),1);
                count++;
            }
            if(numericOption.getName().equals("x")){
                numericOptionValues.put(new NumericOption("x"),0);
                count++;
            }
        }
        assertEquals(3, count);
        return new Configuration(activatedOptions, numericOptionValues);
    }

    /**
     * The estimation belonging to the MinConfiguration above, information got extracted directly from the model.csv
     * Only systemValues implemented so far
     *
     * @return minEstimation
     */
    public Estimation getExpectedMinEstimation(){

        BigDecimal cpu_load_fixed_bd = new BigDecimal(2.22695699444537 + -0.0000190766646872552 * 1);
        BigDecimal cpu_load_bm_bd = new BigDecimal(35.8601227764242 + 0.000355076261559318 * 1);
        BigDecimal energy_consumption_fixed_bd = new BigDecimal(8860.4003024894 + -0.00433864437396869 * 1);
        BigDecimal energy_consumption_bm_bd = new BigDecimal(1211.33207934644 + -0.00930713072932593 * 1);
        BigDecimal compression_size_bd = new BigDecimal(71113559.9036727 + -1033.48475302018 * 1);
        BigDecimal run_time_bd = new BigDecimal(33714.9482813344 + -0.24881685589545 * 1);


        Map<Property, BigDecimal> minSystemValues = new HashMap<>();
        Property cpu_load_fixed = new Property("cpu_load_fixed", "%", "<");
        Property cpu_load_bm = new Property("cpu_load_bm", "%", "<");
        Property energy_consumption_fixed = new Property("energy_consumption_fixed", "Ws","<");
        Property energy_consumption_bm = new Property("energy_consumption_bm", "Ws","<");
        Property compression_size = new Property("compression_size", "B","<");
        Property run_time = new Property("run_time", "ms","<");
        minSystemValues.put(cpu_load_fixed, cpu_load_fixed_bd);
        minSystemValues.put(cpu_load_bm, cpu_load_bm_bd);
        minSystemValues.put(energy_consumption_fixed, energy_consumption_fixed_bd);
        minSystemValues.put(energy_consumption_bm, energy_consumption_bm_bd);
        minSystemValues.put(compression_size, compression_size_bd);
        minSystemValues.put(run_time, run_time_bd);

        Estimation minEstimation = new Estimation(minSystemValues, null);
        return minEstimation;
    }

    /**
     * Getter for the delta value
     *
     * @return 1e-15
     */
    public BigDecimal getDELTA() {
        return DELTA;
    }
}
