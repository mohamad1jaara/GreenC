package swtp7.greenc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swtp7.greenc.Model.Configuration;
import swtp7.greenc.Service.EvaluateService;

/**
 * Controller class that routes the requests made to the /evaluate endpoint to the EvaluateService
 */
@RestController
@RequestMapping(value = "/evaluate")
public class EvaluateController {

    private final EvaluateService evaluateService;

    @Autowired
    public EvaluateController(EvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }

    /**
     * Processing a POST request made to the /evaluate/{softwareSystemName} endpoint to evaluate a Configuration.
     * {softwareSystemName} is a path variable placeholder containing the name of the SoftwareSystem the Configuration
     * belongs to. The request body will contain the Configuration as a json. It is passed to the EvaluateService that
     * will return the evaluated Configuration. If it is null, the request was incorrect so a 404 Not Found Error is
     * sent back.
     *
     * @param softwareSystemName the name of the SoftwareSystem the Configuration belongs to
     * @param configuration      the Configuration that needs to be evaluated
     * @return the evaluated Configuration
     */
    @PostMapping(value = "/{softwareSystemName}")
    ResponseEntity<Configuration> evaluateConfiguration(@PathVariable String softwareSystemName,
                                                        @RequestBody Configuration configuration) {
        Configuration evaluatedConfiguration = evaluateService.evaluateConfiguration(softwareSystemName, configuration);
        if (evaluatedConfiguration != null) {
            return ResponseEntity.ok(evaluatedConfiguration);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
