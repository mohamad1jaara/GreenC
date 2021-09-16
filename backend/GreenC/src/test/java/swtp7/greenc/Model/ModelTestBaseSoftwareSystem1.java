package swtp7.greenc.Model;

import swtp7.greenc.Service.GreenConfiguratorConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Base class for the backend tests regarding SoftwareSystem1 (HSQL_DB)
 */
public abstract class ModelTestBaseSoftwareSystem1{

    /**
     * Delta value for assertEqual with to BigDecimals
     */
    private static final BigDecimal DELTA = new BigDecimal(1e-15);

    /**
     * Method to create a SoftwareSystem instance based on the 3 given xml, dimacs and csv files
     *
     * @return Softwaresystem
     */
    public SoftwareSystem getSoftwareSystem() {

        GreenConfiguratorConfiguration greenConfiguratorConfiguration = new GreenConfiguratorConfiguration();
        SoftwareSystem softwareSystem = greenConfiguratorConfiguration.readSoftwareSystemsFromDisk().get(0);

        return softwareSystem;
    }

    /**
     * Method to create a fully randomized new configuration instance. Every option of the given softwaresystem has a 50/50
     * chance of being activated in the configuration instance.
     *
     * @return Configuration a randomly created configuration instance
     */
    public Configuration getRandomConfiguration(){

        SoftwareSystem softwaresystem = getSoftwareSystem();
        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();

        for(BinaryOption binaryOption: softwaresystem.getBinaryOptions()){
            if(Math.random() < 0.5) {
                activatedOptions.add(binaryOption);
            }
        }
        return new Configuration(activatedOptions, null);
    }

    /**
     * Method to create a valid configuration for our Softwaresystem. The valid configuration got derived from the
     * FeatureModel.dimacs file and a resolution method.
     *
     * @return Configuration a valid configuration
     */
    public Configuration getValidConfiguration() {

        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("encryption"));
        activatedOptions.add(new BinaryOption("crypt_blowfish"));
        activatedOptions.add(new BinaryOption("transaction_control"));
        activatedOptions.add(new BinaryOption("txc_locks"));
        activatedOptions.add(new BinaryOption("table_type"));
        activatedOptions.add(new BinaryOption("cached_tables"));
        activatedOptions.add(new BinaryOption("no_write_delay"));

        return new Configuration(activatedOptions, null);

    }

    /**
     * Method to create an invalid configuration for our Softwaresystem. The valid configuration got derived from the
     * FeatureModel.dimacs file and a resolution method. The validation is not valid, because of the
     * (not memory_tables, not cached_tables) clause from the CNF-formula.
     *
     * @return Configuration a valid configuration
     */
    public Configuration getInvalidConfiguration() {

        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("encryption"));
        activatedOptions.add(new BinaryOption("crypt_blowfish"));
        activatedOptions.add(new BinaryOption("transaction_control"));
        activatedOptions.add(new BinaryOption("txc_locks"));
        activatedOptions.add(new BinaryOption("table_type"));
        activatedOptions.add(new BinaryOption("cached_tables"));
        activatedOptions.add(new BinaryOption("memory_tables"));
        activatedOptions.add(new BinaryOption("no_write_delay"));

        return new Configuration(activatedOptions, null);
    }

    /**
     * Creates a valid configuration with 13 activated options. Every configuration with more activated options
     * wont be valid for our "SoftwareSystem1"
     *
     * @return the configuration with maximum activated options
     */
    public Configuration getMaxConfiguration() {

        List<BinaryOption> activatedOptions = new ArrayList<BinaryOption>();
        activatedOptions.add(new BinaryOption("root"));
        activatedOptions.add(new BinaryOption("compressed_script"));
        activatedOptions.add(new BinaryOption("encryption"));
        activatedOptions.add(new BinaryOption("crypt_blowfish"));
        activatedOptions.add(new BinaryOption("transaction_control"));
        activatedOptions.add(new BinaryOption("txc_locks"));
        activatedOptions.add(new BinaryOption("table_type"));
        activatedOptions.add(new BinaryOption("cached_tables"));
        activatedOptions.add(new BinaryOption("large_cache"));
        activatedOptions.add(new BinaryOption("logging"));
        activatedOptions.add(new BinaryOption("detailed_logging"));
        activatedOptions.add(new BinaryOption("small_log"));
        activatedOptions.add(new BinaryOption("no_write_delay"));

        return new Configuration(activatedOptions, null);
    }

    /**
     * The estimation belonging to the MaxConfiguration above, information got extracted directly from the model.csv
     *
     * @return maxEstimation
     */
    public Estimation getExpectedMaxEstimation(){
        Map<Property, BigDecimal> maxSystemValues = new HashMap<>();

        Property cpu_load = new Property("cpu_load", "%", "<");
        Property energy_consumption = new Property("energy_consumption", "Ws","<");
        Property run_time = new Property("run_time", "s","<");


        maxSystemValues.put(cpu_load, new BigDecimal("10.62625714954148763396"));
        maxSystemValues.put(energy_consumption, new BigDecimal("16.55138616654486235779368167539"));
        maxSystemValues.put(run_time, new BigDecimal("479.3663507086604136098334689019127"));

        List<Influence> partValues = new ArrayList<>();

        //root

        List<BinaryOption> options = new ArrayList<>();
        options.add(new BinaryOption("root"));
        Map<Property, BigDecimal> values = new HashMap<>();
        values.put(energy_consumption, new BigDecimal("13.88643204194815"));
        values.put(run_time, new BigDecimal("262.7674294695168"));
        values.put(cpu_load, new BigDecimal("1.8143897622051424"));

        partValues.add(new Influence(options, null, values));

        //compressed script, crypt blowfish

        options = new ArrayList<>();
        options.add(new BinaryOption("compressed_script"));
        options.add(new BinaryOption("crypt_blowfish"));
        values = new HashMap<>();
        values.put(energy_consumption, new BigDecimal("0.0"));
        values.put(run_time, new BigDecimal("1.6839584020416247"));
        values.put(cpu_load, new BigDecimal("0.06215262445808365"));

        partValues.add(new Influence(options, null, values));

        //encryption, logging, large_cache

        options = new ArrayList<>();
        options.add(new BinaryOption("encryption"));
        options.add(new BinaryOption("logging"));
        options.add(new BinaryOption("large_cache"));
        values = new HashMap<>();
        values.put(energy_consumption, new BigDecimal("0.04400486337657367"));
        values.put(run_time, new BigDecimal("0.0"));
        values.put(cpu_load, new BigDecimal("0.0"));

        partValues.add(new Influence(options, null, values));

        Estimation maxEstimation = new Estimation(maxSystemValues, partValues);
        return maxEstimation;
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
        activatedOptions.add(new BinaryOption("transaction_control"));
        activatedOptions.add(new BinaryOption("txc_mvlocks"));
        activatedOptions.add(new BinaryOption("table_type"));
        activatedOptions.add(new BinaryOption("memory_tables"));

        return new Configuration(activatedOptions, null);
    }

    /**
     * The estimation belonging to the MinConfiguration above, information got extracted directly from the model.csv
     * Only systemValues implemented so far
     *
     * @return minEstimation
     */
    public Estimation getExpectedMinEstimation(){
        Map<Property, BigDecimal> minSystemValues = new HashMap<>();
        Property cpu_load = new Property("cpu_load", "%", "<");
        Property energy_consumption = new Property("energy_consumption", "Ws","<");
        Property run_time = new Property("run_time", "s","<");
        minSystemValues.put(cpu_load, new BigDecimal("1.050574859274822993"));
        minSystemValues.put(energy_consumption, new BigDecimal("13.88643204194815"));
        minSystemValues.put(run_time, new BigDecimal("251.051396889066943495"));
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
