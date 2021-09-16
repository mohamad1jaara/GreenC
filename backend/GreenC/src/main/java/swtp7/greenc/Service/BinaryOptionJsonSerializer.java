package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.BinaryOption;

import java.io.IOException;

/**
 * Class to define the json serialization of the BinaryOption class.
 */
public class BinaryOptionJsonSerializer extends StdSerializer<BinaryOption> {

    public BinaryOptionJsonSerializer() {
        this(null);
    }

    public BinaryOptionJsonSerializer(Class<BinaryOption> t) {
        super(t);
    }

    /**
     * Method that serializes a BinaryOption instance to a json containing the following attributes:
     * - all attributes of the Option class according to its serialization
     * - isOptional: the boolean value whether the Option is optional or not
     *
     * @param binaryOption the BinaryOption to be serialized
     * @param jgen         the JsonGenerator
     * @param provider     the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(BinaryOption binaryOption, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        OptionJsonSerializer optionJsonSerializer = new OptionJsonSerializer();
        optionJsonSerializer.serialize(binaryOption, jgen, provider);

        jgen.writeBooleanField("isOptional", binaryOption.isOptional());

        jgen.writeEndObject();
    }
}
