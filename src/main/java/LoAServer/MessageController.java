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

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/getAllMsgs")
    public ArrayList<Message> getAllMsgs(@PathVariable String gameName) {
        System.out.println(new Gson().toJson(masterDatabase.getSingleMessageDatabase(gameName).getMessages()));
        return masterDatabase.getSingleMessageDatabase(gameName).getMessages();
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getMsg")
    public DeferredResult<ResponseEntity<String>> getMsg(@PathVariable String gameName, @PathVariable String username) {
        return ResponseGenerator.getAsyncUpdate(5000, masterDatabase.getMasterMessageDatabaseDBCM().get(username));
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/sendMsg")
    public void sendMsg(@PathVariable String gameName, @PathVariable String username, @RequestBody Message m) {
        System.out.println("new message: " + m.getMsg());

        masterDatabase.getSingleMessageDatabase(gameName).add(m);

        for (int i = 0; i < masterDatabase.getMasterGameDatabase().getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterMessageDatabaseDBCM().get(masterDatabase.getMasterGameDatabase().getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }
}