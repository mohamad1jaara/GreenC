package swtp7.greenc.Model.XmlParsing;

import swtp7.greenc.Model.BinaryOption;
import swtp7.greenc.Model.NumericOption;
import swtp7.greenc.Model.Option;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to parse a FeatureModel.xml file that stores a list of Options using the JAXB framework.
 * It serves as the only entry point of this package and contains the whole logic.
 * All the other classes are just modeling the tags and objects of the FeatureModel.xml file.
 */
public class XmlParser {

    private final List<BinaryOption> binaryOptions;
    private final List<NumericOption> numericOptions;

    /**
     * Parses a FeatureModel.xml file and stores the BinaryOptions and NumericOptions in their respective attributes.
     * <p>
     * Algorithm: The xml file consists of hierarchically nested objects defined by tags. The other classes in this
     * package are annotated in a way that they'll work as exact counterparts to the xml objects. The JAXB Unmarshaller
     * will read all the relevant information into an instance of the VM class as it is the top level object.
     * The result is then manually parsed into instances of BinaryOption ot NumericOption, requiring two iterations
     * through the xml options. The first iteration will create the Options and set the basic attributes. The second one
     * will set the parent attribute and fills the lists that contain references to other Option instances.
     *
     * @param xmlFile the FeatureModel.xml file to parse
     */
    public XmlParser(File xmlFile) {
        binaryOptions = new ArrayList<>();
        numericOptions = new ArrayList<>();

        VM vm = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(VM.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            vm = (VM) jaxbUnmarshaller.unmarshal(xmlFile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        if (vm != null) {
            Map<String, Option> optionsByName = new HashMap<>();

            if (vm.getBinaryOptions().getConfigurationOptions() != null) {
                for (ConfigurationOption binaryConfigurationOption : vm.getBinaryOptions().getConfigurationOptions()) {
                    String name = binaryConfigurationOption.getName();

                    boolean isOptional = false;

                    switch (binaryConfigurationOption.getOptional()) {
                        case "True":
                            isOptional = true;
                            break;
                        case "False":
                            isOptional = false;
                            break;
                    }

                    BinaryOption binaryOption = new BinaryOption(name, null, new ArrayList<>(), new ArrayList<>(),
                            new ArrayList<>(), isOptional);

                    binaryOptions.add(binaryOption);
                    optionsByName.put(binaryOption.getName(), binaryOption);
                }
            }

            if (vm.getNumericOptions().getConfigurationOptions() != null) {
                for (ConfigurationOption numericConfigurationOption :
                        vm.getNumericOptions().getConfigurationOptions()) {
                    String name = numericConfigurationOption.getName();

                    Integer minValue = Integer.valueOf(numericConfigurationOption.getMinValue());

                    Integer maxValue = Integer.valueOf(numericConfigurationOption.getMaxValue());

                    String stepFunction = numericConfigurationOption.getStepFunction();

                    NumericOption numericOption = new NumericOption(name, null, new ArrayList<>(), new ArrayList<>(),
                            new ArrayList<>(), minValue, maxValue, stepFunction);

                    numericOptions.add(numericOption);
                    optionsByName.put(numericOption.getName(), numericOption);
                }
            }


            List<ConfigurationOption> allConfigurationOptions = new ArrayList<>();
            if (vm.getBinaryOptions().getConfigurationOptions() != null) {
                allConfigurationOptions.addAll(vm.getBinaryOptions().getConfigurationOptions());
            }
            if (vm.getNumericOptions().getConfigurationOptions() != null) {
                allConfigurationOptions.addAll(vm.getNumericOptions().getConfigurationOptions());
            }

            for (ConfigurationOption configurationOption : allConfigurationOptions) {
                Option option = optionsByName.get(configurationOption.getName());

                String parentName = configurationOption.getParent();

                if (optionsByName.containsKey(parentName)) {
                    option.setParent(optionsByName.get(parentName));
                }

                if (option.getParent() != null) {
                    option.getParent().getChildren().add(option);
                }

                if (configurationOption.getImpliedOptions().getOptions() != null) {
                    for (String impliedOptionName : configurationOption.getImpliedOptions().getOptions()) {
                        option.getImpliedOptions().add(optionsByName.get(impliedOptionName));
                    }
                }

                if (configurationOption.getExcludedOptions().getOptions() != null) {
                    for (String excludedOptionName : configurationOption.getExcludedOptions().getOptions()) {
                        option.getExcludedOptions().add(optionsByName.get(excludedOptionName));
                    }
                }

            }
        }

    }

    /**
     * Returns the List of BinaryOptions that was parsed from the FeatureModel.xml file
     *
     * @return the list of BinaryOptions
     */
    public List<BinaryOption> getBinaryOptions() {
        return binaryOptions;
    }

    /**
     * Returns the List of NumericOptions that was parsed from the FeatureModel.xml file
     *
     * @return the list of NumericOptions
     */
    public List<NumericOption> getNumericOptions() {
        return numericOptions;
    }


}
