package swtp7.greenc.Model;

import lombok.Data;
import swtp7.greenc.Model.XmlParsing.XmlParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to read information about a SoftwareSystem that is stored in three files provided by Mondays for Future:
 * - the FeatureModel.xml file that contains a list of all Options
 * - the FeatureModel.dimacs file that contains a list of Clauses
 * - the model.csv file that contains each a list of Properties and Influences (needed e.g. for the InfluenceModel)
 * All methods are very closely tied to the structure and format of the files.
 */
@Data
public class StandardModelReader implements ModelReader {

    private final File featureModelXml;

    private final File featureModelDimacs;

    private final File influenceModelCsv;

    private final XmlParser xmlParser;

    public StandardModelReader(File featureModelXml, File featureModelDimacs, File influenceModelCsv) {
        this.featureModelXml = featureModelXml;
        this.featureModelDimacs = featureModelDimacs;
        this.influenceModelCsv = influenceModelCsv;
        this.xmlParser = new XmlParser(featureModelXml);
    }

    /**
     * Returns the list of BinaryOptions that is stored in the FeatureModel.xml file
     *
     * @return List<BinaryOption> the BinaryOptions of the SoftwareSystem
     */
    @Override
    public List<BinaryOption> getBinaryOptions() {
        return xmlParser.getBinaryOptions();
    }

    /**
     * Returns the list of NumericOptions that is stored in the FeatureModel.xml file
     *
     * @return List<NumericOption> the NumericOptions of the SoftwareSystem
     */
    @Override
    public List<NumericOption> getNumericOptions() {
        return xmlParser.getNumericOptions();
    }

    /**
     * Returns the list of Properties that is stored in the model.csv file
     * <p>
     * Algorithm: The method reads the first two lines of the csv file. In the second line the method detect the first
     * value not equal to 0 or 1. Based on this the method can determine the position of the information of the
     * Properties in the first line. This informaion consist of the name, the unit and the tendency.
     *
     * @return List<Property> the Properties of the SoftwareSystem
     */
    @Override
    public List<Property> getProperties() {
        List<Property> properties = new ArrayList<>();
        String[] header = null;
        String[] dataset = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(influenceModelCsv));
            header = br.readLine().split(",");
            dataset = br.readLine().split(",");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int indexProperties = 0;
        for (int i = 0; i < dataset.length; i++) {
            if (!dataset[i].equals("0") && !dataset[i].equals("1")) {
                indexProperties = i;
                break;
            }
        }
        String[] propertyData = new String[header.length - indexProperties];
        for (int i = 0; i < propertyData.length; i++) {
            propertyData[i] = header[indexProperties + i];
        }
        for (int i = 0; i < propertyData.length; i++) {
            String name = propertyData[i].substring(0, propertyData[i].indexOf("("));
            String unit = propertyData[i].substring(propertyData[i].indexOf("(") + 1, propertyData[i].indexOf(";"));
            String tendency = propertyData[i].substring(propertyData[i].indexOf(";") + 1, propertyData[i].indexOf(")"));
            properties.add(new Property(name, unit, tendency));
        }
        return properties;
    }

    /**
     * Returns the list of Clauses that is stored in the FeatureModel.dimacs file
     * <p>
     * Algorithm: TODO
     *
     * @return List<Clause> the Clauses of the SoftwareSystem
     */
    @Override
    public List<Clause> getClauses() {
        List<Clause> result = new ArrayList<>();
        try {
            List<List<Integer>> readclause = Files.lines(Paths.get(String.valueOf(featureModelDimacs)))
                    .map(line -> line.replaceAll("\\s+", " ").trim())
                    .filter(line -> line.endsWith(" 0"))
                    .map(line -> Arrays.stream(line
                            .substring(0, line.length() - 2)
                            .trim().split("\\s+"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList())
                    ).collect(Collectors.toList());

            for (int i = 0; i < readclause.size(); i++) {
                List<BinaryOption> p = new ArrayList<>();
                List<BinaryOption> n = new ArrayList<>();
                for (int k = 0; k < readclause.get(i).size(); k++) {
                    if (readclause.get(i).get(k) > 0) {
                        p.add(getBinaryOptions().get(Math.abs(readclause.get(i).get(k) - 1)));
                    } else {
                        n.add(getBinaryOptions().get(Math.abs(readclause.get(i).get(k) + 1)));
                    }
                }
                Clause c = new Clause();
                c.setNegativeLiterals(n);
                c.setPositiveLiterals(p);
                result.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Method to parse the csv file to extract a list of all Influences.
     * <p>
     * Algorithm: The method reads line by line the csv file and stores the by 1 valued BinaryOptions with the values for
     * the Properties in an Influence.
     *
     * @return List<Influence> the Influences of the SoftwareSystem
     */
    @Override
    public List<Influence> getInfluences() {
        List<Influence> influences = new ArrayList<>();
        List<BinaryOption> allBinaryOptions = getBinaryOptions();
        List<NumericOption> allNumericOptions = getNumericOptions();
        List<Property> properties = getProperties();
        try {
            BufferedReader br = new BufferedReader(new FileReader(influenceModelCsv));
            String[] header = br.readLine().split(",");
            String line;
            int indexProperties = 0;
            while ((line = br.readLine()) != null) {
                List<BinaryOption> activatedBinaryOptions = new ArrayList<>();
                List<NumericOption> activatedNumericOptions = new ArrayList<>();
                Map<Property, BigDecimal> values = new HashMap<>();
                String[] dataset = line.split(",");
                String[] optionValues;
                String[] propertyValues;
                if (indexProperties == 0) {
                    for (int i = 0; i < dataset.length; i++) {
                        if (!dataset[i].equals("0") && !dataset[i].equals("1")) {
                            indexProperties = i;
                            break;
                        }
                    }
                }
                optionValues = line.substring(0, 2 * indexProperties - 1).split(",");
                propertyValues = line.substring(2 * indexProperties).split(",");
                for (int i = 0; i < optionValues.length; i++) {
                    if (optionValues[i].equals("1")) {
                        if (i < allBinaryOptions.size()) {
                            activatedBinaryOptions.add(allBinaryOptions.get(i));
                        } else {
                            activatedNumericOptions.add(allNumericOptions.get(i - allBinaryOptions.size()));
                        }
                    }
                }
                for (int i = 0; i < propertyValues.length; i++) {
                    String name = header[indexProperties + i].substring(0, header[indexProperties + i].indexOf("("));
                    Property currentProperty = null;
                    for (Property property : properties) {
                        if (property.getName().equals(name)) {
                            currentProperty = property;
                            break;
                        }
                    }
                    values.put(currentProperty, new BigDecimal(propertyValues[i]));
                }
                influences.add(new Influence(activatedBinaryOptions, activatedNumericOptions, values));
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return influences;
    }

}
