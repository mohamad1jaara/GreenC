package swtp7.greenc.Model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import swtp7.greenc.Service.ImprovementRequestJsonDeserializer;
import swtp7.greenc.Service.ImprovementRequestJsonSerializer;

/**
 * Class to store an ImprovementRequest for a given Configuration.
 * An ImprovementRequest consists of the original Configuration and the Property that should be improved.
 */
@Data
@JsonSerialize(using = ImprovementRequestJsonSerializer.class)
@JsonDeserialize(using = ImprovementRequestJsonDeserializer.class)
public class ImprovementRequest {

    private final Property property;

    private final Configuration originalConfiguration;

    private Configuration improvedConfiguration;

}
