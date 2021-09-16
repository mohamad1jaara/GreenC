package swtp7.greenc.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import swtp7.greenc.Service.SoftwareSystemJsonSerializer;

import java.util.List;

/**
 * The SoftwareSystem class; stores a name (HSQL_DB or 7ZIP), a list with all the options (numeric and binary) a user
 * can activate/set
 * (root, logging, encryption etc..);
 * A list with all the properties (cpu_load_fixed, energy_consumption_fixes, run_time etc.)
 * A general model -> location where the analysis of configurations takes place
 */
@JsonSerialize(using = SoftwareSystemJsonSerializer.class)
public class SoftwareSystem {

    @Getter
    private final String name;

    @Getter
    private final List<BinaryOption> binaryOptions;

    @Getter
    private final List<NumericOption> numericOptions;

    @Getter
    private final List<Property> properties;

    @Getter
    private final GeneralModel generalModel;

    public SoftwareSystem(String name, List<BinaryOption> binaryOptions, List<NumericOption> numericOptions,
                          List<Property> properties, List<Clause> clauses, List<Influence> influences) {
        this.name = name;
        this.binaryOptions = binaryOptions;
        this.numericOptions = numericOptions;
        this.properties = properties;
        this.generalModel = new GeneralModel(this, clauses, influences);
        generalModel.getInfluenceModel().setMaxValuesAndScalingFactorsForProperties(properties);
    }

}
