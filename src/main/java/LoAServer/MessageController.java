package LoAServer;

import com.google.gson.Gson;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;

@RestController
public class MessageController {
    private MasterDatabase masterDatabase = MasterDatabase.getInstance();

    @RequestMapping(method=RequestMethod.GET, value="/getAllMsgs")
    public ArrayList<Message> getAllMsgs(@RequestBody String gameName) {
        return masterDatabase.getMasterBroadcastContentManager().get(gameName).getCurrentBroadcastContent().getMessages();
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/getMsg")
    public DeferredResult<ResponseEntity<String>> asyncGetMsg(@PathVariable String gameName) {
        return ResponseGenerator.getAsyncUpdate(5000, masterDatabase.getMasterBroadcastContentManager().get(gameName));
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/sendMsg")
    public void sendMsg(@PathVariable String gameName, @RequestBody Message m) {
        System.out.println(m.getMsg());

        MessageDatabase msgDatabaseDeepCopy = new Gson().fromJson(new Gson().toJson(masterDatabase.getMasterBroadcastContentManager().get(gameName).getCurrentBroadcastContent()), MessageDatabase.class);

        msgDatabaseDeepCopy.add(m);
        masterDatabase.getMasterBroadcastContentManager().get(gameName).updateBroadcastContent(msgDatabaseDeepCopy);
    }
}