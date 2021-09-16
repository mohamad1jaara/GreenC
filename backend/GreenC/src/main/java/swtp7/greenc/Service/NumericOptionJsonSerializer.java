package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.NumericOption;

import java.io.IOException;

/**
 * Class to define the json serialization of the NumericOption class.
 */
public class NumericOptionJsonSerializer extends StdSerializer<NumericOption> {

    public NumericOptionJsonSerializer() {
        this(null);
    }

    public NumericOptionJsonSerializer(Class<NumericOption> t) {
        super(t);
    }

    /**
     * Method that serializes a NumericOption instance to a json containing the following attributes:
     * - all attributes of the Option class according to its serialization
     * - minValue: The minimal possible value
     * - maxValue: The maximal possible value
     * - stepFunction: Function to define valid values within the range
     *
     * @param numericOption the NumericOption to be serialized
     * @param jgen          the JsonGenerator
     * @param provider      the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(NumericOption numericOption, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        OptionJsonSerializer optionJsonSerializer = new OptionJsonSerializer();
        optionJsonSerializer.serialize(numericOption, jgen, provider);

        jgen.writeNumberField("minValue", numericOption.getMinValue());

        jgen.writeNumberField("maxValue", numericOption.getMaxValue());

        jgen.writeStringField("stepFunction", numericOption.getStepFunction().getName());

        jgen.writeEndObject();
    }
}
