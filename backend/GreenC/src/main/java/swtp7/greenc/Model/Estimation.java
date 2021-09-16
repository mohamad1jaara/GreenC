package swtp7.greenc.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import swtp7.greenc.Service.EstimationJsonSerializer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Class to store the estimation of a given configuration. Each instance of this class consists of systemValues and
 * partValues. SystemValues stores the estimated computing time, energy consumption etc of the whole configuration;
 * PartValues stores a list of subsets of set options from the configuration with the given values as a list of
 * influences.
 */
@Data
@JsonSerialize(using = EstimationJsonSerializer.class)
public class Estimation {

    /**
     * A map containing BigDecimal values belonging to properties
     */
    private Map<Property, BigDecimal> systemValues;

    /**
     * A list of all relevant Influences for the estimation of a Configuration.
     */
    private List<Influence> partValues;

    public Estimation(Map<Property, BigDecimal> systemvalues, List<Influence> partValues) {
        this.systemValues = systemvalues;
        this.partValues = partValues;
    }
}
