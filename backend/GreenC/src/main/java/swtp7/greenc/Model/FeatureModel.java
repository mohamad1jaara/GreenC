package swtp7.greenc.Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class checks whether a given configuration is valid and finds valid alternative configurations for invalid ones. It
 * contains a list of clauses related to the implemented software system information.
 */
@Data
public class FeatureModel {

    /**
     * the related GeneralModel
     */
    private final GeneralModel generalModel;

    /**
     * the list of clauses
     */
    private final List<Clause> clauses;

    /**
     * Method to check whether a given configuration is valid or not under the assumption that
     * the configuration and the feature model both belong to the same software system. A configuration is valid if and
     * only if all of the clauses of the {@link this.clauses} list are satisfied (evaluated as positive).
     * <p>
     * Algorithm: Checks if the clauses are satisfied. A clause is satisfied if either at least one of its
     * positive literals is an activated Option or at least one of its negative literals is not an activated Option.
     *
     * @param configuration the Configuration to be validated
     * @return boolean the boolean value whether the given Configuration is valid or not
     */
    public boolean checkValidity(Configuration configuration) {
        for (Clause clause : clauses) {
            boolean clauseIsSatisfied = false;
            for (Option positiveLiteral : clause.getPositiveLiterals()) {
                if (configuration.getActivatedBinaryOptions().contains(positiveLiteral)) {
                    clauseIsSatisfied = true;
                    break;
                }
            }
            for (Option negativeLiteral : clause.getNegativeLiterals()) {
                if (!(configuration.getActivatedBinaryOptions().contains(negativeLiteral))) {
                    clauseIsSatisfied = true;
                    break;
                }
            }
            if (!clauseIsSatisfied) {
                return false;
            }
        }

        if (configuration.getNumericOptionValues() != null) {
            for (NumericOption numericOption: configuration.getNumericOptionValues().keySet()) {
                for (NumericOption numericOption1 : generalModel.getSoftwareSystem().getNumericOptions()) {
                    if (numericOption.getName().equals(numericOption1.getName())) {
                        int minValue = numericOption1.getMinValue();
                        int maxValue = numericOption1.getMaxValue();
                        StepFunction stepFunction = numericOption1.getStepFunction();
                        if (!(stepFunction.checkValidity(configuration.getNumericOptionValues().get(numericOption),
                                minValue, maxValue))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Method to compute a valid alternative to a given invalid Configuration that is as similar as possible. As there
     * are different ways to measure similarity this method follows a prioritization: As many Options as possible should
     * stay activated in the alternative Configuration compared to the original one. The reason behind this is the
     * assumption, that every activated Option is activated for a reason.
     * <p>
     * At the moment the method always returns a minimal Configuration of SoftwareSystem1 to deliver mock-up data.
     * TODO implement method properly(?)
     *
     * @param configuration the invalid Configuration to find an alternative to
     * @return an alternative Configuration that is valid
     */
    public Configuration findValidAlternative(Configuration configuration) {
        List<BinaryOption> additionallyActivatedOptions = new ArrayList<>();

        List<Clause> positiveClauses = getPositiveClauses();
        List<Clause> negativeClauses = getNegativeClauses();
        List<Clause> mixedClauses = getMixedClauses();

        for (Clause clause : positiveClauses) {
            if (clause.getPositiveLiterals().size() == 1 && !configuration.getActivatedBinaryOptions().contains(clause.getPositiveLiterals().get(
                    0))) {
                additionallyActivatedOptions.add(clause.getPositiveLiterals().get(0));
            }
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            List<BinaryOption> allActivatedOptions = new ArrayList<>();
            allActivatedOptions.addAll(configuration.getActivatedBinaryOptions());
            allActivatedOptions.addAll(additionallyActivatedOptions);
            for (Clause clause : getUnsatisfiedClauses(mixedClauses, allActivatedOptions)) {
                if (clause.getPositiveLiterals().size() == 1 && clause.getNegativeLiterals().size() == 1) {
                    additionallyActivatedOptions.add(clause.getPositiveLiterals().get(0));
                    changed = true;
                }
            }
        }

        List<BinaryOption> allActivatedOptions = new ArrayList<>();
        allActivatedOptions.addAll(configuration.getActivatedBinaryOptions());
        allActivatedOptions.addAll(additionallyActivatedOptions);


        for (Clause clause : getUnsatisfiedClauses(clauses, allActivatedOptions)) {
            if (clause.getPositiveLiterals().size() >= 2) {
                allActivatedOptions.add(clause.getPositiveLiterals().get(0));
            }
        }

        for (Clause clause : getUnsatisfiedClauses(negativeClauses, allActivatedOptions)) {
            if (clause.getNegativeLiterals().size() == 2) {
                if (!clauseIsSatisfied(clause, allActivatedOptions)) {
                    for (int i = 0; i < clause.getNegativeLiterals().size(); i++) {
                        Option currentOption = clause.getNegativeLiterals().get(i);
                        List<BinaryOption> tempOptions = new ArrayList<>(allActivatedOptions);
                        tempOptions.remove(currentOption);
                        if (getUnsatisfiedClauses(clauses, tempOptions).size() < getUnsatisfiedClauses(clauses,
                                allActivatedOptions).size()) {
                            allActivatedOptions.remove(currentOption);
                            break;
                        }
                    }
                }
            }
        }
        Map<NumericOption, Integer> newNumericOptionValues = new HashMap<>();
        
        if (configuration.getNumericOptionValues() != null) {
            for (NumericOption numericOption : configuration.getNumericOptionValues().keySet()) {
                for (NumericOption numericOption1 : generalModel.getSoftwareSystem().getNumericOptions()) {
                    if (numericOption.equals(numericOption1)) {
                        if (numericOption1.getStepFunction().checkValidity(configuration.getNumericOptionValues().get(numericOption),
                                numericOption1.getMinValue(), numericOption1.getMaxValue())) {
                            newNumericOptionValues.put(numericOption, configuration.getNumericOptionValues().get(numericOption));
                        } else {
                            newNumericOptionValues.put(numericOption, numericOption1.getStepFunction().findClosestAlternative(
                                    configuration.getNumericOptionValues().get(numericOption), numericOption1.getMinValue(),
                                    numericOption1.getMaxValue()));
                        }
                        break;
                    }
                }
            }
        }

        Configuration alternativeConfiguration = new Configuration(allActivatedOptions, newNumericOptionValues);
        alternativeConfiguration.setIsValid(true);
        return alternativeConfiguration;
    }

    /**
     * Help method to get all unsatisfied clauses of an configuration
     *
     * @param clauses
     * @param activatedOptions
     * @return unsatisfied clauses
     */
    private List<Clause> getUnsatisfiedClauses(List<Clause> clauses, List<BinaryOption> activatedOptions) {
        List<Clause> unsatisfiedClauses = new ArrayList<>();
        for (Clause clause : clauses) {
            if (!clauseIsSatisfied(clause, activatedOptions)) {
                unsatisfiedClauses.add(clause);
            }
        }
        return unsatisfiedClauses;
    }

    /**
     * Help method to get all clauses with only positive literals
     *
     * @return positive clauses
     */
    private List<Clause> getPositiveClauses() {
        List<Clause> positiveClauses = new ArrayList();
        for (Clause clause : clauses) {
            if (clause.getNegativeLiterals().size() == 0) {
                positiveClauses.add(clause);
            }
        }
        return positiveClauses;
    }

    /**
     * Help method to get all clauses with only negative literals
     *
     * @return negative clauses
     */
    private List<Clause> getNegativeClauses() {
        List<Clause> negativeClauses = new ArrayList();
        for (Clause clause : clauses) {
            if (clause.getPositiveLiterals().size() == 0) {
                negativeClauses.add(clause);
            }
        }
        return negativeClauses;
    }

    /**
     * Help method to get all clauses with positive and negative literals
     *
     * @return mixed clauses
     */
    private List<Clause> getMixedClauses() {
        List<Clause> mixedClauses = new ArrayList();
        for (Clause clause : clauses) {
            if (clause.getNegativeLiterals().size() > 0 && clause.getPositiveLiterals().size() > 0) {
                mixedClauses.add(clause);
            }
        }
        return mixedClauses;
    }

    /**
     * Help method to find out if a given clause is valid under a set of positive & negative axioms (activated options)
     *
     * @param clause           a set of axioms connected with disjuntions
     * @param activatedOptions
     * @return
     */
    private boolean clauseIsSatisfied(Clause clause, List<BinaryOption> activatedOptions) {
        boolean clauseIsSatisfied = false;
        for (Option positiveLiteral : clause.getPositiveLiterals()) {
            if (activatedOptions.contains(positiveLiteral)) {
                clauseIsSatisfied = true;
                break;
            }
        }
        for (Option negativeLiteral : clause.getNegativeLiterals()) {
            if (!activatedOptions.contains(negativeLiteral)) {
                clauseIsSatisfied = true;
                break;
            }
        }
        return clauseIsSatisfied;
    }
}




