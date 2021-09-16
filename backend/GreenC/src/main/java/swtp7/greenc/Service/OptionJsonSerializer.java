package swtp7.greenc.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import swtp7.greenc.Model.Option;

import java.io.IOException;

/**
 * Class to define the json serialization of the Option class.
 */
public class OptionJsonSerializer extends StdSerializer<Option> {

    public OptionJsonSerializer() {
        this(null);
    }

    public OptionJsonSerializer(Class<Option> t) {
        super(t);
    }

    /**
     * Method that serializes an Option instance to a json containing the following attributes:
     * - name: the name of the Option
     * - parent: the name of the parent
     * - children: a list of the names of the children
     * - impliedOptions: a list of the names of the implied Options
     * - excludedOptions: a list of the names of the excluded Options
     * As the Option class is abstract, this serialization will be used by its subclasses, so no tags indicating the
     * start or end of this object will be written.
     *
     * @param option   the Option to be serialized
     * @param jgen     the JsonGenerator
     * @param provider the SerializerProvider
     * @throws IOException when there is an error writing the json
     */
    @Override
    public void serialize(Option option, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeStringField("name", option.getName());

        if (option.getParent() != null) {
            jgen.writeStringField("parent", option.getParent().getName());
        } else {
            jgen.writeNullField("parent");
        }

        jgen.writeArrayFieldStart("children");
        for (Option child : option.getChildren()) {
            jgen.writeString(child.getName());
        }
        jgen.writeEndArray();

        jgen.writeArrayFieldStart("impliedOptions");
        for (Option impliedOption : option.getImpliedOptions()) {
            jgen.writeString(impliedOption.getName());
        }
        jgen.writeEndArray();

        jgen.writeArrayFieldStart("excludedOptions");
        for (Option excludedOption : option.getExcludedOptions()) {
            jgen.writeString(excludedOption.getName());
        }
        jgen.writeEndArray();

    }
}
