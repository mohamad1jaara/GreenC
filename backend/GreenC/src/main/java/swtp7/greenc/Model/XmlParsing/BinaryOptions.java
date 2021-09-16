package swtp7.greenc.Model.XmlParsing;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Class to model the binaryOptions tag of the FeatureModel.xml file
 */
@XmlRootElement(name = "binaryOptions")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
class BinaryOptions {

    @XmlElement(name = "configurationOption", type = ConfigurationOption.class)
    private List<ConfigurationOption> configurationOptions;

}
