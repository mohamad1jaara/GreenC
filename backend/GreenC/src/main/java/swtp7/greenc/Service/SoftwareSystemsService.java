package swtp7.greenc.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtp7.greenc.Model.GreenConfigurator;
import swtp7.greenc.Model.SoftwareSystem;

import java.util.List;

/**
 * Service class to return Information about stored SoftwareSystems. Holds an instance of GreenConfigurator to access
 * the Model layer.
 */
@Service
public class SoftwareSystemsService {

    private final GreenConfigurator greenConfigurator;

    @Autowired
    public SoftwareSystemsService(GreenConfigurator greenConfigurator) {
        this.greenConfigurator = greenConfigurator;
    }

    /**
     * @return the list of SoftwareSystems stored in the GreenConfigurator instance
     */
    public List<SoftwareSystem> returnSoftwareSystems() {
        return greenConfigurator.getSoftwareSystems();
    }
}
