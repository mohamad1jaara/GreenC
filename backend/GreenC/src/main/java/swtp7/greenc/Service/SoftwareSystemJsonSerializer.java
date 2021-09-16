package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.BinaryOption;
import swtp7.greenc.Model.NumericOption;
import swtp7.greenc.Model.Property;
import swtp7.greenc.Model.SoftwareSystem;

import java.io.IOException;

/**
 * Class to define the json serialization of the SoftwareSystem class.
 */
public class SoftwareSystemJsonSerializer extends StdSerializer<SoftwareSystem> {

    public SoftwareSystemJsonSerializer() {
        this(null);
    }

    public SoftwareSystemJsonSerializer(Class<SoftwareSystem> t) {
        super(t);
    }

    /**
     * Method that serializes a SoftwareSystem instance to a json containing the following attributes:
     * - name: the name of the SoftwareSystem
     * - binaryOptions: the list of BinaryOptions
     * - numericOptions: the list of NumericOptions
     * - properties: the list of properties
     *
     * @param softwareSystem the softwareSystem to be serialized
     * @param jgen           the JsonGenerator
     * @param provider       the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(SoftwareSystem softwareSystem, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        jgen.writeStringField("name", softwareSystem.getName());

        jgen.writeArrayFieldStart("binaryOptions");
        for (BinaryOption binaryOption : softwareSystem.getBinaryOptions()) {
            jgen.writeObject(binaryOption);
        }
        jgen.writeEndArray();

        jgen.writeArrayFieldStart("numericOptions");
        for (NumericOption numericOption : softwareSystem.getNumericOptions()) {
            jgen.writeObject(numericOption);
        }
        jgen.writeEndArray();

        jgen.writeArrayFieldStart("properties");
        for (Property property : softwareSystem.getProperties()) {
            jgen.writeObject(property);
        }
        jgen.writeEndArray();

        jgen.writeEndObject();
    }
}
