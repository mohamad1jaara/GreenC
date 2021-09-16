package swtp7.greenc.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import swtp7.greenc.Service.BinaryOptionJsonSerializer;

import java.util.List;

/**
 * Class to store a BinaryOption of a SoftwareSystem.
 * A BinaryOption is an Option that can either be activated or deactivated.
 * It is either optional or not.
 */
@JsonSerialize(using = BinaryOptionJsonSerializer.class)
public class BinaryOption extends Option {
    /**
     * Boolean value whether the BinaryOption is optional
     */
    @Getter
    @Setter
    private boolean isOptional;

    public BinaryOption(String name) {
        super(name);
    }

    public BinaryOption(String name, Option parent, List<Option> children, List<Option> impliedOptions,
                        List<Option> excludedOptions, boolean isOptional) {
        super(name, parent, children, impliedOptions, excludedOptions);
        this.isOptional = isOptional;
    }

    public BinaryOption(String name, Option parent, boolean isOptional) {
        super(name, parent, null, null, null);
        this.isOptional = isOptional;
    }

}
