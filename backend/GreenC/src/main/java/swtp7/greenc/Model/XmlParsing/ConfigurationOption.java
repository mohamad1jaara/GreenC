package swtp7.greenc.Model.XmlParsing;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to model the configurationOption tag of the FeatureModel.xml file
 */
@XmlRootElement(name = "configurationOption")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
class ConfigurationOption {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "parent")
    private String parent;

    @XmlElement(name = "impliedOptions", type = ImpliedOptions.class)
    private ImpliedOptions impliedOptions;

    @XmlElement(name = "excludedOptions", type = ExcludedOptions.class)
    private ExcludedOptions excludedOptions;

    @XmlElement(name = "optional")
    private String optional;

    @XmlElement(name = "minValue")
    private String minValue;

    @XmlElement(name = "maxValue")
    private String maxValue;

    @XmlElement(name = "stepFunction")
    private String stepFunction;

}
