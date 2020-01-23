package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class MessageController {
    private MasterDatabase masterDatabase = MasterDatabase.getInstance();

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/getMsg")
    public DeferredResult<ResponseEntity<String>> asyncGetMsg(@PathVariable String gameName) {
        masterDatabase.addMessageDatabase("game1"); // TEST DELETE AFTER!!!!!!!!!!!!!!!!!!!!!

        return ResponseGenerator.getAsyncUpdate(5000, masterDatabase.getMasterBroadcastContentManager().get(gameName));
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/sendMsg")
    public void sendMsg(@PathVariable String gameName, @RequestBody Message m) {
        for (MessageDatabase msgDatabase : masterDatabase.getMasterMessageDatabase()) {
            if (msgDatabase.getGameName().equals(gameName)) {
                msgDatabase.add(m);
                masterDatabase.getMasterBroadcastContentManager().get(gameName).updateBroadcastContent(masterDatabase.getSingleMessageDatabase(gameName));
            }
        }
    }
}