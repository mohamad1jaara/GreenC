package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.NumericOption;
import swtp7.greenc.Model.Property;

import java.io.IOException;

/**
 * Class to define the json serialization of the Property class.
 */
public class PropertyJsonSerializer extends StdSerializer<Property> {

    public PropertyJsonSerializer() {
        this(null);
    }

    public PropertyJsonSerializer(Class<Property> t) {
        super(t);
    }

    /**
     * Method that serializes a Property instance to a json containing the following attributes:
     * - name: the name of the Property
     * - unit: the unit of the Property
     * - tendency: the tendency of the Property
     * - maxValue: an upper boundary for it's maximum value
     * - maxValueScalingFactors: factors for each NumericOption that scale the maxValue
     *
     * @param property the Property to be serialized
     * @param jgen     the JsonGenerator
     * @param provider the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(Property property, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        jgen.writeStringField("name", property.getName());

        jgen.writeStringField("unit", property.getUnit());

        jgen.writeStringField("tendency", property.getTendency());

        jgen.writeNumberField("maxValue", property.getMaxValue());

        jgen.writeFieldName("maxValueScalingFactors");
        jgen.writeStartObject();
        for (NumericOption numericOption : property.getMaxValueScalingFactors().keySet()) {
            jgen.writeNumberField(numericOption.getName(), property.getMaxValueScalingFactors().get(numericOption));
        }
        jgen.writeEndObject();

        jgen.writeEndObject();
    }
}
