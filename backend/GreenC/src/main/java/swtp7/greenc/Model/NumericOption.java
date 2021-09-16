package swtp7.greenc.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import swtp7.greenc.Service.NumericOptionJsonSerializer;

import java.util.List;

/**
 * Class to store a NumericOption of a SoftwareSystem.
 * A NumericOption is an Option for which a numerical value can be set.
 * It has a minimal and maximal value and a step function defining which values are valid.
 */
@JsonSerialize(using = NumericOptionJsonSerializer.class)
public class NumericOption extends Option {

    @Getter
    @Setter
    private Integer minValue;

    @Getter
    @Setter
    private Integer maxValue;

    @Getter
    @Setter
    private StepFunction stepFunction;

    public NumericOption(String name) {
        super(name);
    }

    public NumericOption(String name, Option parent, List<Option> children, List<Option> impliedOptions,
                         List<Option> excludedOptions, Integer minValue, Integer maxValue, String stepFunctionName) {
        super(name, parent, children, impliedOptions, excludedOptions);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepFunction = new StepFunction(stepFunctionName);
    }

}
