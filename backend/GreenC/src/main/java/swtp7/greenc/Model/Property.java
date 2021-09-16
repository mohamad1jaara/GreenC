package swtp7.greenc.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import swtp7.greenc.Service.PropertyJsonSerializer;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Class to store the properties of a given SoftwareSystem.
 * In our case, properties are energy_consumption (unit: "Ws, tendency: "<""), run_time (unit: "s", tendency "<") and
 * cpu_load (unit: "%", tendency "<") for HSQL_DB and run_time, compression_size etc for the 7ZIP software system.
 * The tendency String is either a "<" for properties where a smaller value is wanted or ">" if bigger values are
 * considered better.
 * The maxValue is an upper boundary for the value of the property in any possible configuration
 * where the values for NumericOptions are less or equal to 1.
 * The maxValueScalingFactors map holds a factor for each NumericOption that can be multiplied with its value in a
 * Configuration and then be added to the maxValue to give the proper upper boundary.
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonSerialize(using = PropertyJsonSerializer.class)
public class Property {

    @EqualsAndHashCode.Include
    private final String name;

    private String unit;

    private String tendency;

    private BigDecimal maxValue;

    private Map<NumericOption, BigDecimal> maxValueScalingFactors;

    public Property(String name, String unit, String tendency) {
        this.name = name;
        this.unit = unit;
        this.tendency = tendency;
    }

}
