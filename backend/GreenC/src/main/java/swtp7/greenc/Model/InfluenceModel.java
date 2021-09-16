package swtp7.greenc.Model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to evaluate valid configurations. Contains the list of influences of the SoftwareSystem it belongs to.
 */
@RequiredArgsConstructor
public class InfluenceModel {

    private final GeneralModel generalModel;

    @Getter
    private final List<Influence> influences;

    /**
     * Method to compute the Estimation of a given Configuration.
     * <p>
     * Algorithm: All Influences from the {@link this.influences} list are checked whether they are relevant for the
     * Configuration. An Influence is relevant only if all of its BinaryOptions are activated in the Configuration. In
     * addition, an Influence becomes irrelevant if one of its NumericOption has the value 0 in the Configuration. This
     * is because the value would otherwise be multiplied with all Property values turning them to 0 as well.
     * All relevant Influences have to be adjusted with the values of the NumericOptions of the Configuration. For every
     * NumericOption that is part of the influence the values of the Influence have to be multiplied with the value of
     * the NumericOption.
     * All adjusted Influences are added to the partValues list of the Estimation. For each Property, the sum of the
     * values of all adjusted influences will be stored in the systemValues Map.
     *
     * @param configuration the Configuration to be estimated
     * @return the Estimation of the Configuration
     */
    public Estimation estimateConfiguration(Configuration configuration) {
        Map<Property, BigDecimal> systemValues = new HashMap<>();
        List<Influence> partValues = new ArrayList<>();

        for (Influence influence : influences) {
            boolean influenceIsRelevant = true;

            for (BinaryOption binaryInfluenceOption : influence.getBinaryOptions()) {
                if (!configuration.getActivatedBinaryOptions().contains(binaryInfluenceOption)) {
                    influenceIsRelevant = false;
                    break;
                }
            }

            if (influenceIsRelevant) {
                for (NumericOption numericInfluenceOption : influence.getNumericOptions()) {
                    if (configuration.getNumericOptionValues().get(numericInfluenceOption) == 0) {
                        influenceIsRelevant = false;
                    }
                }
            }

            if (influenceIsRelevant) {
                Map<Property, BigDecimal> adjustedValues = new HashMap<Property, BigDecimal>();
                for (Property property : influence.getValues().keySet()) {
                    BigDecimal copyOfOriginalValue = new BigDecimal(influence.getValues().get(property).toString());
                    adjustedValues.put(property, copyOfOriginalValue);
                }

                for (NumericOption numericInfluenceOption : influence.getNumericOptions()) {
                    BigDecimal numericOptionValue = new BigDecimal(
                            configuration.getNumericOptionValues().get(numericInfluenceOption).toString());
                    for (Property property : adjustedValues.keySet()) {
                        BigDecimal newAdjustedValue = adjustedValues.get(property).multiply(numericOptionValue);
                        adjustedValues.put(property, newAdjustedValue);
                    }
                }

                Influence adjustedInfluence = new Influence(influence.getBinaryOptions(), influence.getNumericOptions(),
                        adjustedValues);

                for (Property property : adjustedInfluence.getValues().keySet()) {
                    if (systemValues.containsKey(property)) {
                        systemValues.put(property,
                                systemValues.get(property).add(adjustedInfluence.getValues().get(property)));
                    } else {
                        systemValues.put(property, adjustedInfluence.getValues().get(property));
                    }
                }
                partValues.add(adjustedInfluence);
            }
        }
        return new Estimation(systemValues, partValues);
    }

    /**
     * Method to set the maxValue and maxValueScalingFactors attribute of given properties which should belong to the
     * same SoftwareSystem as this InfluenceModel.
     * For every Property all positive values from all Influences are summed up. The result can be used as upper
     * bound as the Property will never reach a higher value.
     * For each Property the scaling factors of all NumericOptions are calculated by summing up all values that are
     * positive and values where there are no BinaryOptions present in the Influence. This gives a correct scaling
     * factor for the max value.
     *
     * @param properties the list of properties which maxValues and maxValueScalingFactors are to set
     */
    public void setMaxValuesAndScalingFactorsForProperties(List<Property> properties) {
        Map<Property, BigDecimal> maxValuesForProperties = new HashMap<>();
        Map<Property, Map<NumericOption, BigDecimal>> scalingFactorsForProperties = new HashMap<>();

        for (Influence influence : influences) {
            for (Map.Entry<Property, BigDecimal> propertyValue : influence.getValues().entrySet()) {
                Property property = propertyValue.getKey();
                BigDecimal value = propertyValue.getValue();
                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    if (maxValuesForProperties.containsKey(property)) {
                        maxValuesForProperties.put(property, maxValuesForProperties.get(property).add(value));
                    } else {
                        maxValuesForProperties.put(property, value);
                    }
                }

                for (NumericOption numericOption : influence.getNumericOptions()) {
                    if (influence.getBinaryOptions().isEmpty() || value.compareTo(BigDecimal.ZERO) > 0) {
                        if (!scalingFactorsForProperties.containsKey(property)) {
                            Map<NumericOption, BigDecimal> scalingFactors = new HashMap<>();
                            scalingFactors.put(numericOption, value);
                            scalingFactorsForProperties.put(property, scalingFactors);
                        } else {
                            Map<NumericOption, BigDecimal> scalingFactors = scalingFactorsForProperties.get(property);
                            if (scalingFactors.containsKey(numericOption)) {
                                scalingFactors.put(numericOption, scalingFactors.get(numericOption).add(value));
                            } else {
                                scalingFactors.put(numericOption, value);
                            }
                        }
                    }
                }

            }
        }

        for (Property property : properties) {
            if (maxValuesForProperties.containsKey(property)) {
                property.setMaxValue(maxValuesForProperties.get(property));
            }
            if (scalingFactorsForProperties.containsKey(property)) {
                property.setMaxValueScalingFactors(scalingFactorsForProperties.get(property));
            } else {
                property.setMaxValueScalingFactors(new HashMap<>());
            }
        }
    }
}
