package swtp7.greenc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swtp7.greenc.Model.ImprovementRequest;
import swtp7.greenc.Service.ImproveService;

/**
 * Controller class that routes the requests made to the /improve endpoint to the ImproveService
 */
@RestController
@RequestMapping(value = "/improve")
public class ImproveController {

    private final ImproveService improveService;

    @Autowired
    public ImproveController(ImproveService improveService) {
        this.improveService = improveService;
    }

    /**
     * Processing a POST request made to the /improve/{softwareSystemName} endpoint to improve a Configuration.
     * {softwareSystemName} is a path variable placeholder containing the name of the SoftwareSystem the Configuration
     * belongs to. The request body will contain the ImprovementRequest as a json. It is passed to the ImproveService
     * that will return the processed ImprovementRequest. If it is null, the request was incorrect so a 404 Not Found
     * Error is sent back.
     *
     * @param softwareSystemName the name of the SoftwareSystem the ImprovementRequest belongs to
     * @param improvementRequest the ImprovementRequest that needs to be processed
     * @return the processed ImprovementRequest
     */
    @PostMapping(value = "/{softwareSystemName}")
    ResponseEntity<ImprovementRequest> improveConfiguration(
            @PathVariable String softwareSystemName, @RequestBody ImprovementRequest improvementRequest) {
        ImprovementRequest improvedConfiguration = improveService.improveConfiguration(softwareSystemName,
                improvementRequest);
        if (improvedConfiguration != null) {
            return ResponseEntity.ok(improvedConfiguration);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
