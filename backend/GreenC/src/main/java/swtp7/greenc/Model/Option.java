package swtp7.greenc.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Class to store an Option of a SoftwareSystem.
 * Each Option has a unique name in the context of the SoftwareSystem.
 * It has a parent (that may be null) and a list of children.
 * It has list of excluded Options that may not be activated if this one is.
 * It has a list of implied Options that have to be activated if this one is.
 * Known extending classes are BinaryOption and NumericOption.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Option {

    @EqualsAndHashCode.Include
    private final String name;

    private Option parent;

    private List<Option> children;

    private List<Option> impliedOptions;

    private List<Option> excludedOptions;

}
