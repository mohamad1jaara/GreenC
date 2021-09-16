package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.ImprovementRequest;

import java.io.IOException;

/**
 * Class to define the json serialization of the ImprovementRequest class.
 */
public class ImprovementRequestJsonSerializer extends StdSerializer<ImprovementRequest> {

    public ImprovementRequestJsonSerializer() {
        this(null);
    }

    public ImprovementRequestJsonSerializer(Class<ImprovementRequest> t) {
        super(t);
    }

    /**
     * Method that serializes a ImprovementRequest instance to a json containing the following attributes:
     * - property: the Property to be improved
     * - originalConfiguration: the original Configuration to be improved
     * - improvedConfiguration: the improved Configuration
     *
     * @param improvementRequest the ImprovementRequest to be serialized
     * @param jgen               the JsonGenerator
     * @param provider           the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(ImprovementRequest improvementRequest, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        jgen.writeStringField("property", improvementRequest.getProperty().getName());

        jgen.writeObjectField("originalConfiguration", improvementRequest.getOriginalConfiguration());

        jgen.writeFieldName("improvedConfiguration");
        if (improvementRequest.getImprovedConfiguration() != null) {
            jgen.writeObject(improvementRequest.getImprovedConfiguration());
        } else {
            jgen.writeNull();
        }

        jgen.writeEndObject();
    }
}
