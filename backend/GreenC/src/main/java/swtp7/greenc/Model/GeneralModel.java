package swtp7.greenc.Model;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.*;

/**
 * Class to manage the FeatureModel and the InfluenceModel regarding a specific SoftwareSystem. Handles evaluation and
 * improvement of Configurations
 */
public class GeneralModel {

    private static final int MAX_DIFFERENCE_ALLOWED_FOR_IMPROVED_ALTERNATIVE = 3;

    @Getter
    private final SoftwareSystem softwareSystem;
    @Getter
    private final FeatureModel featureModel;
    @Getter
    private final InfluenceModel influenceModel;

    public GeneralModel(SoftwareSystem softwareSystem, List<Clause> clauses, List<Influence> influences) {
        this.softwareSystem = softwareSystem;
        this.featureModel = new FeatureModel(this, clauses);
        this.influenceModel = new InfluenceModel(this, influences);
    }

    /**
     * Method to evaluate a Configuration.
     * <p>
     * Algorithm: The checkValidity method of the FeatureModel is called to set the isValid attribute. If the
     * Configuration is valid the estimateConfiguration method of the InfluenceModel is called to set the Estimation
     * attribute. If the Configuration is invalid the findValidAlternative method of the FeatureModel is called to set
     * the alternativeConfiguration attribute. The estimation attribute of the alternative Configuration is set as
     * before.
     *
     * @param configuration the Configuration to evaluate
     * @return the evaluated Configuration
     */
    public Configuration evaluateConfiguration(Configuration configuration) {
        boolean configurationIsValid = featureModel.checkValidity(configuration);
        configuration.setIsValid(configurationIsValid);

        if (configurationIsValid) {
            configuration.setEstimation(influenceModel.estimateConfiguration(configuration));
        } else {
            configuration.setEstimation(null);
            Configuration alternativeConfiguration = featureModel.findValidAlternative(configuration);
            alternativeConfiguration.setEstimation(influenceModel.estimateConfiguration(alternativeConfiguration));
            configuration.setAlternativeConfiguration(alternativeConfiguration);
        }

        return configuration;
    }

    /**
     * Method to find an alternative Configuration with an improved Property.
     * Algorithm: The method determine all candidates of BinaryOptions which could be activated or deactivated based on
     * the original Configuration. Next all combinations of additional activated Options and deactivated Options
     * concerning the original Configuration with maximal three changes absolut will be tested whether the Property
     * gets improved. The best alternative Configuration will be stored in the ImprovementRequest.
     * If there is no better configuration, the method store the original Configuration.
     *
     * @param improvementRequest The improvementRequest stores the original Configuration and the property to be improved
     * @return the ImprovementRequest with the stored improved Configuration
     */
    public ImprovementRequest findImprovedAlternative(ImprovementRequest improvementRequest) {
        Configuration originalConfiguration = evaluateConfiguration(improvementRequest.getOriginalConfiguration());
        if (originalConfiguration.getIsValid()) {
            Property property = softwareSystem.getProperties().get(
                    softwareSystem.getProperties().indexOf(improvementRequest.getProperty()));
            List<Influence> positiveValueInfluences = getPositiveValueInfluences(property,
                    influenceModel.getInfluences());
            List<Influence> negativeValueInfluences = getNegativeValueInfluences(property,
                    influenceModel.getInfluences());

            Set<BinaryOption> activationCandidates = getActivationCandidates(originalConfiguration, property,
                    positiveValueInfluences, negativeValueInfluences);
            Set<BinaryOption> deactivationCandidates = new HashSet<>(originalConfiguration.getActivatedBinaryOptions());

            for (int allowedDifference = 1; allowedDifference <= MAX_DIFFERENCE_ALLOWED_FOR_IMPROVED_ALTERNATIVE;
                 allowedDifference++) {
                Configuration currentBest = originalConfiguration;
                for (int numberOfActivations = 0; numberOfActivations <= allowedDifference; numberOfActivations++) {
                    int numberOfDeactivations = allowedDifference - numberOfActivations;
                    Set<Set<BinaryOption>> activationCombinations = new HashSet<>();
                    for (Set<BinaryOption> subset : Sets.powerSet(activationCandidates)) {
                        if (subset.size() == numberOfActivations) {
                            activationCombinations.add(subset);
                        }
                    }

                    Set<Set<BinaryOption>> deactivationCombinations = new HashSet<>();
                    for (Set<BinaryOption> subset : Sets.powerSet(deactivationCandidates)) {
                        if (subset.size() == numberOfDeactivations) {
                            deactivationCombinations.add(subset);
                        }
                    }

                    for (Set<BinaryOption> activationCombination : activationCombinations) {
                        for (Set<BinaryOption> deactivationCombination : deactivationCombinations) {
                            List<BinaryOption> candidateOptions =
                                    new ArrayList<>(originalConfiguration.getActivatedBinaryOptions());
                            candidateOptions.addAll(activationCombination);
                            candidateOptions.removeAll(deactivationCombination);

                            Configuration candidate = new Configuration(candidateOptions,
                                    originalConfiguration.getNumericOptionValues());
                            candidate.setIsValid(featureModel.checkValidity(candidate));

                            if (candidate.getIsValid()) {
                                candidate.setEstimation(influenceModel.estimateConfiguration(candidate));

                                if (property.getTendency().equals("<")) {
                                    if (candidate.getEstimation().getSystemValues().get(property).compareTo(
                                            currentBest.getEstimation().getSystemValues().get(property)) < 0) {
                                        currentBest = candidate;
                                    }

                                } else if (property.getTendency().equals(">")) {
                                    if (candidate.getEstimation().getSystemValues().get(property).compareTo(
                                            currentBest.getEstimation().getSystemValues().get(property)) > 0) {
                                        currentBest = candidate;
                                    }
                                }
                            }
                        }
                    }
                }
                if (currentBest != originalConfiguration) {
                    improvementRequest.setImprovedConfiguration(currentBest);
                    return improvementRequest;
                }
            }
        }
        improvementRequest.setImprovedConfiguration(originalConfiguration);
        return improvementRequest;
    }

    /**
     * Help method to get all Influences with a negative value for the Property
     * @param property the considered Property
     * @param influences List with all Influences
     * @return a list with the found Influences
     */
    private List<Influence> getNegativeValueInfluences(Property property, List<Influence> influences) {
        List<Influence> negativeValueInfluences = new ArrayList<>();
        for (Influence influence : influences) {
            if (influence.getValues().get(property).compareTo(BigDecimal.ZERO) < 0) {
                negativeValueInfluences.add(influence);
            }
        }
        return negativeValueInfluences;
    }

    /**
     * Help method to get all Influences with a positive value for the Property
     * @param property the considered Property
     * @param influences List with all Influences
     * @return a list with the found Influences
     */
    private List<Influence> getPositiveValueInfluences(Property property, List<Influence> influences) {
        List<Influence> positiveValueInfluences = new ArrayList<>();
        for (Influence influence : influences) {
            if (influence.getValues().get(property).compareTo(BigDecimal.ZERO) > 0) {
                positiveValueInfluences.add(influence);
            }
        }
        return positiveValueInfluences;
    }

    /**
     * Help method to find the BinaryOptions which can be additionally activated to a given Configuration.
     * @param configuration the considered Configuration
     * @param property the Property that should be improved in the findImprovedAlternative() method
     * @param positiveValueInfluences a list of the Influences with a positive value for the Property
     * @param negativeValueInfluences a list of the Influences with a negative value for the Property
     * @return a Set with the found BinaryOptions
     */
    private Set<BinaryOption> getActivationCandidates(Configuration configuration, Property property,
                                                      List<Influence> positiveValueInfluences,
                                                      List<Influence> negativeValueInfluences) {
        Set<BinaryOption> activationCandidates = new HashSet<>();

        List<Influence> listToQuery = null;
        if (property.getTendency().equals("<")) {
            listToQuery = negativeValueInfluences;
        } else if (property.getTendency().equals(">")) {
            listToQuery = positiveValueInfluences;
        }

        if (listToQuery != null) {
            for (Influence badInfluence : listToQuery) {
                for (BinaryOption influenceOption : badInfluence.getBinaryOptions())
                    if (!configuration.getActivatedBinaryOptions().contains(influenceOption)) {
                        activationCandidates.add(influenceOption);
                    }
            }
            return activationCandidates;
        } else {
            return Collections.emptySet();
        }
    }

}
