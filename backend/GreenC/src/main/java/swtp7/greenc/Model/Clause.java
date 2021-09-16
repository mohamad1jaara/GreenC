package swtp7.greenc.Model;

import lombok.Data;

import java.util.List;

/**
 * Class to store logical constraints in CNF. Each instance of the Clause class is a disjunction of literals.
 * The literals are separated in positive an negative literals.
 */
@Data
public class Clause {
    /**
     * the positive literals of the clause
     */
    private List<BinaryOption> positiveLiterals;
    /**
     * the negated literals of the clause
     */
    private List<BinaryOption> negativeLiterals;

}
