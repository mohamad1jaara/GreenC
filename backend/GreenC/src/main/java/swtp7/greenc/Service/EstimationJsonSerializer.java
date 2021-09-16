package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.Estimation;
import swtp7.greenc.Model.Influence;
import swtp7.greenc.Model.Property;

import java.io.IOException;

/**
 * Class to define the json serialization of the Estimation class.
 */
public class EstimationJsonSerializer extends StdSerializer<Estimation> {

    public EstimationJsonSerializer() {
        this(null);
    }

    public EstimationJsonSerializer(Class<Estimation> t) {
        super(t);
    }

    /**
     * Methods that serializes a Estimation instance to a json containing the following attributes:
     * - systemValues: a map of Properties and their values
     * - partValues: a list of Influences
     *
     * @param estimation the Estimation to be serialized
     * @param jgen       the JsonGenerator
     * @param provider   the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(Estimation estimation, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        jgen.writeFieldName("systemValues");
        jgen.writeStartObject();
        for (Property property : estimation.getSystemValues().keySet()) {
            jgen.writeNumberField(property.getName(), estimation.getSystemValues().get(property));
        }
        jgen.writeEndObject();

        jgen.writeFieldName("partValues");
        jgen.writeStartArray();
        for (Influence influence : estimation.getPartValues()) {
            jgen.writeObject(influence);
        }
        jgen.writeEndArray();

        jgen.writeEndObject();

    }

}
