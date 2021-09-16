package swtp7.greenc.Model;

import java.util.List;

/**
 * Interface to return information about a SoftwareSystem read from varying sources.
 * It allows loose coupling to the format in which information about the SoftwareSystem is given.
 * Implementations will read such information and provide each a list of BinaryOptions, NumericOptions, Properties,
 * Clauses and Influences.
 */
public interface ModelReader {

    /**
     * Returns the list of BinaryOptions of a SoftwareSystem
     *
     * @return List<BinaryOption> the list of BinaryOptions of the SoftwareSystem
     */
    List<BinaryOption> getBinaryOptions();

    /**
     * Returns the list of NumericOptions of a SoftwareSystem
     *
     * @return List<NumericOption> the list of NumericOptions of the SoftwareSystem
     */
    List<NumericOption> getNumericOptions();

    /**
     * Returns the list of Properties of a SoftwareSystem
     *
     * @return List<Property> the Properties of the SoftwareSystem
     */
    List<Property> getProperties();

    /**
     * Returns the list of Clauses of a SoftwareSystem
     *
     * @return List<Clause> the Clauses of the SoftwareSystem
     */
    List<Clause> getClauses();

    /**
     * Returns the list of Influences of a SoftwareSystem
     *
     * @return List<Influence> the Influences of the SoftwareSystem
     */
    List<Influence> getInfluences();

}
