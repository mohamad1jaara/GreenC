package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.Configuration;
import swtp7.greenc.Model.NumericOption;
import swtp7.greenc.Model.Option;

import java.io.IOException;

/**
 * Class to define the json serialization of the Configuration class.
 */
public class ConfigurationJsonSerializer extends StdSerializer<Configuration> {

    public ConfigurationJsonSerializer() {
        this(null);
    }

    public ConfigurationJsonSerializer(Class<Configuration> t) {
        super(t);
    }

    /**
     * Method that serializes a Configuration instance to a json containing the following attributes:
     * - activatedOptions: a list of the names of the activated Options
     * - numericOptionValues: a map holding the integer values that are set for the NumericOptions
     * - isValid: the boolean value whether the Configuration is valid
     * - estimation: the corresponding Estimation instance
     * - alternativeConfiguration: the alternative Configuration instance
     *
     * @param configuration the Configuration to be serialized
     * @param jgen          the JsonGenerator
     * @param provider      the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(Configuration configuration, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        jgen.writeArrayFieldStart("activatedBinaryOptions");
        for (Option option : configuration.getActivatedBinaryOptions()) {
            jgen.writeString(option.getName());
        }
        jgen.writeEndArray();

        jgen.writeFieldName("numericOptionValues");
        jgen.writeStartObject();
        for (NumericOption numericOption : configuration.getNumericOptionValues().keySet()) {
            jgen.writeNumberField(numericOption.getName(), configuration.getNumericOptionValues().get(numericOption));
        }
        jgen.writeEndObject();

        jgen.writeBooleanField("isValid", configuration.getIsValid());

        jgen.writeFieldName("estimation");
        jgen.writeObject(configuration.getEstimation());

        jgen.writeFieldName("alternativeConfiguration");
        jgen.writeObject(configuration.getAlternativeConfiguration());

        jgen.writeEndObject();
    }
}
