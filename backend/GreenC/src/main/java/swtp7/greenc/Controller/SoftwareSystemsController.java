package swtp7.greenc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swtp7.greenc.Model.SoftwareSystem;
import swtp7.greenc.Service.SoftwareSystemsService;

import java.util.List;

/**
 * Controller class that routes the requests made to the /softwaresystems endpoint to the SoftwareSystemsService
 */
@RestController
@RequestMapping(value = "/softwaresystems")
public class SoftwareSystemsController {

    private final SoftwareSystemsService softwareSystemsService;

    @Autowired
    public SoftwareSystemsController(SoftwareSystemsService softwareSystemsService) {
        this.softwareSystemsService = softwareSystemsService;
    }

    /**
     * Returns the list of SoftwareSystems that are obtained from the SoftwareSystemService
     *
     * @return the list of SoftwareSystems
     */
    @GetMapping
    public ResponseEntity<List<SoftwareSystem>> returnSoftwareSystems() {
        return ResponseEntity.ok(softwareSystemsService.returnSoftwareSystems());
    }

}
