package patmal.course.enigma.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/enigma/ping")
    public String ping() {
        return "pong";
    }
}