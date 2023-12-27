package cz.cvut.fit.tjv.social_network.web_client.web_client;

import cz.cvut.fit.tjv.social_network.web_client.model.UserDto;
import cz.cvut.fit.tjv.social_network.web_client.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/{username}/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUser(Model model, @PathVariable String username){
        this.userService.setCurrentUser(username);
        var userOpt = userService.readUserById(username);
        if (userOpt.isEmpty())
            //todo
            return "index";
        model.addAttribute("alllikes",(userService.sumAllLikes(username)));
        model.addAttribute("colikes",userService.sumCoWorkerLikes(username));
        model.addAttribute("postlikes", userService.sumPostLikes(username));
        model.addAttribute("friends",userService.getFriends(username));
        model.addAttribute("followers",userOpt.get().getFollower().size());
        model.addAttribute("user", userOpt.get());
        return "users";
    }

    @GetMapping("edit")
    public String editShow(Model model, @PathVariable("username") String username){
        var currOpt = userService.getCurrentUser();
        if(currOpt.isEmpty())
            return "index";
        if(!currOpt.get().getUsername().equals(username)){
            return getUser(model,username);
        }
        model.addAttribute("user",currOpt.get());
        return "editUser";
    }
    @PostMapping("edit")
    public String editSubmit(Model model, @PathVariable("username")String username, @ModelAttribute UserDto user){
        if(!userService.isCurrentUser())
            return getUser(model,username);
        userService.update(user);
        return getUser(model,username);
    }

}
