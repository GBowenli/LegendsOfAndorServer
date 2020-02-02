package LoAServer;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoAServer {
    public static void main (String[] args) {
        SpringApplication.run(LoAServer.class, args);
    }
}
