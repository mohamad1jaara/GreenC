package swtp7.greenc.Model;
import lombok.Data;

/**
 * Class representing the StepFunction of a NumericOption instance. So far there are only 3 different StepFunctions in the
 * 7zip SoftwareSystem. "x+2", "BlockSize * 2"  and "Files + 10". The CheckValidity method checks, if a given value is valid.
 */
@Data
public class StepFunction {

    String name;

    public StepFunction(String name) {
        this.name = name;
    }

    /**
     * Checks, if a given value is valid for the StepFunction instance:
     * First: Check, if minValue<= value <= maxValue
     * Second: Check, if the value is part of the domain of the StepFunction
     *
     * @param value the intValue to consider
     * @param minValue the minValue of the StepFunction
     * @param maxValue the maxValue of the StepFunction
     * @return True, if the value is valid
     */
    public boolean checkValidity(int value, int minValue, int maxValue){

        if(minValue <= value && value <= maxValue) {

            if (name.equals("x + 2")) {
                if ((value - minValue) % 2 == 0 && (value - minValue >= 0)) {
                    return true;
                }
                return false;
            }
            if (name.equals("BlockSize * 2")) {
                while (value > 0) {
                    if (value == minValue) {
                        return true;
                    } else {
                        if (value % 2 == 0) {
                            value = value / 2;
                        } else {
                            return false;
                        }
                    }
                }
                return false;
            }
            if (name.equals("Files + 10")) {
                if ((value - minValue) % 10 == 0 && (value - minValue >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Help method for the FindValidAlternative method of the FeatureModel class; Returns a valid value for the given StepFunction;
     * If the given value is not in the interval [minValue,maxValue] the method returns the minValue, if value<minValue.
     * If value>maxValue, the maxValue will be returned. If the value is in the interval but not in the domain of the stepFunction,
     * the method checks the next decremented and the next incremented value etc.
     * @param value the value to be checked
     * @param minValue the minValue of the StepFunction
     * @param maxValue the maxValue of the StepFunction
     * @return
     */
    int findClosestAlternative(int value, int minValue, int maxValue){
        if (value<minValue){
            return minValue;
        }
        if(value>maxValue){
            return maxValue;
        }
        int decrement = value;
        int increment = value;
        while(true){
            if(checkValidity(decrement, minValue, maxValue)){
                return decrement;
            }
            if(checkValidity(increment, minValue, maxValue)){
                return increment;
            }
            decrement --;
            increment ++;
        }
    }
}
