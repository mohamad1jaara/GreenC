package swtp7.greenc.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import swtp7.greenc.Service.InfluenceJsonSerializer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Class to store influences of single options or entities of options. Influences can be understood as snippets from the
 * model.csv file; each influence represents a line, every  or numericOption with a 1 set in the csv file is included
 * in one of the two lists; the values Map represent the influences as PropertyValues
 * <p>
 * Example instance regarding HSQL_DB SoftwareSystem: options = {logging, encryption}
 * values  = {{cpu_load, 0.0},
 * {run_time, 0.02},
 * {energy_consumption, 0.31}}
 * (fake values)
 * -> logging and encryption doesnt change the cpu but slow the program down and increase the energy consumption
 */
@Data
@JsonSerialize(using = InfluenceJsonSerializer.class)
public class Influence {

    private final List<BinaryOption> binaryOptions;

    private final List<NumericOption> numericOptions;

    private final Map<Property, BigDecimal> values;

}
