package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class GameController {
    private MasterDatabase masterDatabase = MasterDatabase.getInstance();

    @RequestMapping(method= RequestMethod.GET, value="/{username}/getGameUpdate")
    public DeferredResult<ResponseEntity<String>> getGameUpdate(@PathVariable String username) {
        return ResponseGenerator.getAsyncUpdate(5000, masterDatabase.getMasterGameBCM().get(username));
    }
}
