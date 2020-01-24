package LoAServer;

import com.google.gson.Gson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;

@RestController
public class MessageController {
    private MasterDatabase masterDatabase = MasterDatabase.getInstance();

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/getAllMsgs")
    public ArrayList<Message> getAllMsgs(@PathVariable String gameName) {
        return masterDatabase.getSingleMessageDatabase(gameName).getMessages();
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getMsg")
    public DeferredResult<ResponseEntity<String>> asyncGetMsg(@PathVariable String gameName, @PathVariable String username) {
        return ResponseGenerator.getAsyncUpdate(5000, masterDatabase.getMasterMessageDatabaseDBCM().get(username));
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/sendMsg")
    public void sendMsg(@PathVariable String gameName, @PathVariable String username, @RequestBody Message m) {
        System.out.println("new message: " + m.getMsg());

        masterDatabase.getSingleMessageDatabase(gameName).add(m);

        // CHANGE THIS!!! when add create/join game do all players in GAME!!!!
        MessageDatabase msgDatabaseDeepCopy = new Gson().fromJson(new Gson().toJson(masterDatabase.getMasterMessageDatabaseDBCM().get("User 1").getCurrentBroadcastContent()), MessageDatabase.class);
        msgDatabaseDeepCopy.add(m);
        masterDatabase.getMasterMessageDatabaseDBCM().get("User 1").updateBroadcastContent(msgDatabaseDeepCopy);

        MessageDatabase msgDatabaseDeepCopy2 = new Gson().fromJson(new Gson().toJson(masterDatabase.getMasterMessageDatabaseDBCM().get("User 2").getCurrentBroadcastContent()), MessageDatabase.class);
        msgDatabaseDeepCopy2.add(m);
        masterDatabase.getMasterMessageDatabaseDBCM().get("User 2").updateBroadcastContent(msgDatabaseDeepCopy2);
    }
}