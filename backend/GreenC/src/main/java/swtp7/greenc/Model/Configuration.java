package swtp7.greenc.Model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import swtp7.greenc.Service.ConfigurationJsonDeserializer;
import swtp7.greenc.Service.ConfigurationJsonSerializer;

import java.util.List;
import java.util.Map;

/**
 * Class to store a Configuration of a SoftwareSystem.
 */
@Data
@JsonSerialize(using = ConfigurationJsonSerializer.class)
@JsonDeserialize(using = ConfigurationJsonDeserializer.class)
public class Configuration {

    /**
     * A list of the BinaryOptions that are activated in this Configuration.
     */
    private final List<BinaryOption> activatedBinaryOptions;

    /**
     * A map holding the integer values that are set for the NumericOptions.
     */
    private final Map<NumericOption, Integer> numericOptionValues;

    /**
     * A boolean value whether this Configuration is valid or not.
     */
    private Boolean isValid;

    /**
     * The corresponding Estimation instance. Will be null if isValid is false.
     */
    private Estimation estimation;

    /**
     * A valid alternative if this Configuration is invalid. Will be null if isValid is true.
     */
    private Configuration alternativeConfiguration;

}
