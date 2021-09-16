package swtp7.greenc.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtp7.greenc.Model.Configuration;
import swtp7.greenc.Model.GreenConfigurator;
import swtp7.greenc.Model.SoftwareSystem;

/**
 * Service class to evaluate a Configuration. Holds an instance of GreenConfigurator to access the Model layer.
 */
@Service
public class EvaluateService {

    private final GreenConfigurator greenConfigurator;

    @Autowired
    public EvaluateService(GreenConfigurator greenConfigurator) {
        this.greenConfigurator = greenConfigurator;
    }

    /**
     * Evaluates a certain Configuration of a SoftwareSystem. Passes the Configuration to the GeneralModel of the
     * SoftwareSystems which will return the evaluated Configuration.
     * <p>
     * Returns null if no SoftwareSystem with the given name exists.
     *
     * @param softwareSystemName the name of the SoftwareSystem
     * @param configuration      the Configuration to evaluate
     * @return the evaluated Configuration
     */
    public Configuration evaluateConfiguration(String softwareSystemName, Configuration configuration) {
        for (SoftwareSystem softwareSystem : greenConfigurator.getSoftwareSystems()) {
            if (softwareSystem.getName().equals(softwareSystemName)) {
                return softwareSystem.getGeneralModel().evaluateConfiguration(configuration);
            }
        }
        return null;
    }

}
