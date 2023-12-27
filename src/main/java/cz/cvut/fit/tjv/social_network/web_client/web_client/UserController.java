package cz.cvut.fit.tjv.social_network.web_client.web_client;

import cz.cvut.fit.tjv.social_network.web_client.model.UserDto;
import cz.cvut.fit.tjv.social_network.web_client.service.UserService;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping()
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
        //todo
        userService.setCurrentUser("test1");
    }
    private void current(Model model){
        if(userService.getCurrentUser().isEmpty())
            return ;
        model.addAttribute("current",userService.getCurrentUser().get().getUsername());
    }
    @GetMapping("/{username}/")
    public String getUser(Model model, @PathVariable String username){
        current(model);
        var userOpt = userService.readUserById(username);
        if (userOpt.isEmpty())
            //todo
            return "index";
        var isFollowed = userService.isFollowed(username);
        model.addAttribute("isFollowed",isFollowed);
        model.addAttribute("alllikes",(userService.sumAllLikes(username)));
        model.addAttribute("colikes",userService.sumCoWorkerLikes(username));
        model.addAttribute("postlikes", userService.sumPostLikes(username));
        model.addAttribute("friends",userService.getFriends(username));
        model.addAttribute("user", userOpt.get());
        return "users";
    }

    @GetMapping("/{username}/edit")
    public String editShow(Model model, @PathVariable("username") String username){
        this.current(model);
        var currOpt = userService.getCurrentUser();
        if(currOpt.isEmpty())
            return "index";
        if(!currOpt.get().getUsername().equals(username)){
            return getUser(model,username);
        }
        model.addAttribute("user",currOpt.get());
        return "editUser";
    }
    @PostMapping("/{username}/edit")
    public String editSubmit(Model model, @PathVariable("username")String username, @ModelAttribute UserDto user){
        if(!userService.isCurrentUser())
            return getUser(model,username);
        userService.update(user);
        return getUser(model,username);
    }
    @PostMapping("/{username}/follow")
    public String follow(Model model, @PathVariable("username") String username){
        userService.follow(username);
        return getUser(model, username);
    }
    @PostMapping("/{username}/unfollow")
    public String unfollow(Model model, @PathVariable("username") String username){
        userService.unfollow(username);
        return getUser(model,username);
    }
    @GetMapping("/{username}/followed")
    public String getFollowed(Model model, @PathVariable("username") String username){
        current(model);
        model.addAttribute("usersText","Followed");
        model.addAttribute("users",userService.getFollowed(username));
        return "usersCollection";
    }
    @GetMapping("/{username}/followers")
    public String getFollowers(Model model, @PathVariable("username") String username){
        current(model);
        model.addAttribute("usersText","Followers");
        model.addAttribute("users",userService.getFollowers(username));
        return "usersCollection";
    }
    @GetMapping("/{username}/friends")
    public String getFriends(Model model, @PathVariable("username") String username){
        current(model);
        model.addAttribute("usersText","Friends");
        model.addAttribute("users",userService.getFriends(username));
        return "usersCollection";
    }
    @GetMapping("all-users")
    public String getAll(Model model){
        current(model);
        model.addAttribute("usersText","All users");
        model.addAttribute("users",userService.getAll());
        return "usersCollection";
    }
    @GetMapping("/{username}/delete")
    public String delete(Model model, @PathVariable("username") String username){
        this.current(model);
        var currentUserOpt = userService.getCurrentUser();
        if(currentUserOpt.isEmpty())
            return "index";
        return "deleteUser";
    }
    @PostMapping("/{username}/delete")
    public String deleteSubmit(Model model, @PathVariable("username") String username,  String control){
        this.current(model);
        var currentUserOpt = userService.getCurrentUser();
        if(currentUserOpt.isEmpty())
            return "index";
        var currentUser= currentUserOpt.get();
        if(!username.equals(currentUser.getUsername()))
            return "users";
        if(control.equals(username)) {
            userService.delete(username);
            return "index";
        }
        return "editUser";
    }

}
