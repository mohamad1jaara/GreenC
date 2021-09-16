package swtp7.greenc.Model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class to store the SoftwareSystems that can be configurated with the GreenConfigurator.
 */
@Component
@Scope("singleton")
public class GreenConfigurator {

    /**
     * A list of all SoftwareSystems
     */
    @Getter
    private List<SoftwareSystem> softwareSystems;

    @Autowired
    public void setSoftwareSystems(List<SoftwareSystem> softwareSystems) {
        this.softwareSystems = softwareSystems;
    }

}
