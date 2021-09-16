package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import swtp7.greenc.Model.Configuration;
import swtp7.greenc.Model.ImprovementRequest;
import swtp7.greenc.Model.Property;

import java.io.IOException;

/**
 * Class to define the json deserialization of the ImprovementRequest class.
 */
public class ImprovementRequestJsonDeserializer extends StdDeserializer<ImprovementRequest> {

    public ImprovementRequestJsonDeserializer() {
        this(null);
    }

    public ImprovementRequestJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * Method that deserializes a json holding a ImprovementRequest into a Java instance containing the following
     * attributes:
     * - property: the property to be improved
     * - originalConfiguration: the original Configuration to be improved
     *
     * @param jp   the JsonParser
     * @param ctxt the DeserializationContext
     * @return the ImprovementRequest instance stored in the json
     * @throws IOException when there is an error reading the json
     */
    @Override
    public ImprovementRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        JsonNode propertyNode = node.path("property");
        String propertyName = propertyNode.asText();
        Property property = new Property(propertyName);

        JsonNode originalConfigurationNode = node.path("originalConfiguration");
        Configuration originalConfiguration = new ObjectMapper().treeToValue(originalConfigurationNode,
                Configuration.class);

        return new ImprovementRequest(property, originalConfiguration);
    }
}
