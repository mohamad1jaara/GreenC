package swtp7.greenc.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtp7.greenc.Model.GreenConfigurator;
import swtp7.greenc.Model.ImprovementRequest;
import swtp7.greenc.Model.SoftwareSystem;

/**
 * Service class to process an ImprovementRequest. Holds an instance of GreenConfigurator to access the Model layer.
 */
@Service
public class ImproveService {

    private final GreenConfigurator greenConfigurator;

    @Autowired
    public ImproveService(GreenConfigurator greenConfigurator) {
        this.greenConfigurator = greenConfigurator;
    }

    /**
     * Evaluates an ImprovementRequest related to a SoftwareSystem. Passes the Configuration to the GeneralModel of the
     * SoftwareSystems which will return the processed ImprovementRequest.
     * <p>
     * Returns null if no SoftwareSystem with the given name exists.
     *
     * @param softwareSystemName the name of the SoftwareSystem
     * @param improvementRequest the ImprovementRequest to be processed
     * @return the processed ImprovementRequest
     */
    public ImprovementRequest improveConfiguration(String softwareSystemName, ImprovementRequest improvementRequest) {
        for (SoftwareSystem softwareSystem : greenConfigurator.getSoftwareSystems()) {
            if (softwareSystem.getName().equals(softwareSystemName)) {
                return softwareSystem.getGeneralModel().findImprovedAlternative(improvementRequest);
            }
        }
        return null;
    }
}
