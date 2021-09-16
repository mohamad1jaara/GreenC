package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.Influence;
import swtp7.greenc.Model.Option;
import swtp7.greenc.Model.Property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to define the json serialization of the Influence class.
 */
public class InfluenceJsonSerializer extends StdSerializer<Influence> {

    public InfluenceJsonSerializer() {
        this(null);
    }

    public InfluenceJsonSerializer(Class<Influence> t) {
        super(t);
    }

    /**
     * Method that serializes an Influence instance to a json containing the following attributes:
     * - options: a list of all Options, both BinaryOptions and NumericOptions without differentiating between them
     * - values: a map of Properties and their values
     *
     * @param influence the Influence to be serialized
     * @param jgen      the JsonGenerator
     * @param provider  the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(Influence influence, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        List<Option> allOptions = new ArrayList<>(influence.getBinaryOptions());
        allOptions.addAll(influence.getNumericOptions());

        jgen.writeArrayFieldStart("options");
        for (Option option : allOptions) {
            jgen.writeString(option.getName());
        }
        jgen.writeEndArray();

        jgen.writeFieldName("values");
        jgen.writeStartObject();
        for (Property property : influence.getValues().keySet()) {
            jgen.writeNumberField(property.getName(), influence.getValues().get(property));
        }
        jgen.writeEndObject();

        jgen.writeEndObject();
    }

}
