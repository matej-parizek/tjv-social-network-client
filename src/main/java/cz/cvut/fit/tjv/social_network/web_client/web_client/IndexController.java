package cz.cvut.fit.tjv.social_network.web_client.web_client;

import cz.cvut.fit.tjv.social_network.web_client.api.UserClient;
import cz.cvut.fit.tjv.social_network.web_client.service.UserService;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index() {
        return "index";
    }
}

