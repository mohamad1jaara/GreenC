package swtp7.greenc.Model.XmlParsing;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Class to model the excludedOptions tag of the FeatureModel.xml file
 */
@XmlRootElement(name = "excludedOptions")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
class ExcludedOptions {

    @XmlElement(name = "options")
    private List<String> options;

}
