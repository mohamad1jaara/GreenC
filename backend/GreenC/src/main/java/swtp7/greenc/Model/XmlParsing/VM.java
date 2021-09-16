package swtp7.greenc.Model.XmlParsing;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * Class to model the vm tag of the FeatureModel.xml file
 */
@XmlRootElement(name = "vm")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
class VM {

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "binaryOptions", type = BinaryOptions.class)
    private BinaryOptions binaryOptions;

    @XmlElement(name = "numericOptions", type = NumericOptions.class)
    private NumericOptions numericOptions;

}
