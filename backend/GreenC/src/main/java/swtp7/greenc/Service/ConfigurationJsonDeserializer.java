package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import swtp7.greenc.Model.BinaryOption;
import swtp7.greenc.Model.Configuration;
import swtp7.greenc.Model.NumericOption;

import java.io.IOException;
import java.util.*;

/**
 * Class to define the json deserialization of the Configuration class.
 */
public class ConfigurationJsonDeserializer extends StdDeserializer<Configuration> {

    public ConfigurationJsonDeserializer() {
        this(null);
    }

    public ConfigurationJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * Method that deserializes a json holding a Configuration into a Java instance containing the following attributes:
     * - activatedOptions: a list of the activated BinaryOptions
     * - numericOptionValues: a map holding the integer values that are set for the NumericOptions
     *
     * @param jp   the JsonParser
     * @param ctxt the DeserializationContext
     * @return the Configuration instance stored in the json
     * @throws IOException when there is an error reading the json
     */
    @Override
    public Configuration deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        List<BinaryOption> activatedBinaryOptions = new ArrayList<>();

        Iterator<JsonNode> activatedBinaryOptionNode = node.path("activatedBinaryOptions").elements();
        while (activatedBinaryOptionNode.hasNext()) {
            String activatedBinaryOptionName = activatedBinaryOptionNode.next().textValue();
            BinaryOption activatedBinaryOption = new BinaryOption(activatedBinaryOptionName);
            activatedBinaryOptions.add(activatedBinaryOption);
        }

        Map<NumericOption, Integer> numericOptionValues = new HashMap<>();

        Iterator<Map.Entry<String, JsonNode>> numericOptionValuesNode = node.path("numericOptionValues").fields();
        while (numericOptionValuesNode.hasNext()) {
            Map.Entry<String, JsonNode> numericOptionValuePair = numericOptionValuesNode.next();
            String numericOptionName = numericOptionValuePair.getKey();
            Integer value = 0;
            if(!(numericOptionValuePair.getValue().textValue().equals(""))){
                value = Integer.parseInt(numericOptionValuePair.getValue().textValue());
            }
            NumericOption numericOption = new NumericOption(numericOptionName);
            numericOptionValues.put(numericOption, value);
        }

        return new Configuration(activatedBinaryOptions, numericOptionValues);
    }
}
