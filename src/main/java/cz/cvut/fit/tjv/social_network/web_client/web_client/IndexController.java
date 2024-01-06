package cz.cvut.fit.tjv.social_network.web_client.web_client;

import cz.cvut.fit.tjv.social_network.web_client.exceptions.UserNotExistException;
import cz.cvut.fit.tjv.social_network.web_client.model.AuthorizationDto;
import cz.cvut.fit.tjv.social_network.web_client.model.FollowedPosts;
import cz.cvut.fit.tjv.social_network.web_client.model.StringField;
import cz.cvut.fit.tjv.social_network.web_client.model.UserDto;
import cz.cvut.fit.tjv.social_network.web_client.service.AuthorizationService;
import cz.cvut.fit.tjv.social_network.web_client.service.PostService;
import cz.cvut.fit.tjv.social_network.web_client.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/")
public class IndexController {
    private UserService userService;
    private PostService postService;
    private AuthorizationService authorizationService;

    public IndexController(UserService userService, PostService postService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.postService = postService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public String index(Model model) {
        if(userService.getCurrentUsername()==null)
            return "redirect:/login";
        var posts = postService.getFollowedPosts();
        current(model);
        Set<FollowedPosts> post = new HashSet<FollowedPosts>(posts);
        model.addAttribute("posts",post);
        return "followedPosts";

    }
    @PostMapping("/{author}/{id}/like")
    public String like(Model model, @PathVariable("author") String username, @PathVariable("id") Long id){
        current(model);
        if(userService.getCurrentUsername()==null)
            return "redirect:/login";
        postService.like(username,id);
        return "redirect:/";
    }
    @PostMapping("/{author}/{id}/unlike")
    public String unlike(Model model, @PathVariable("author") String username, @PathVariable("id") Long id){
        if(userService.getCurrentUsername()==null)
            return "redirect:/login";
        current(model);
        postService.unlike(username,id);
        return "redirect:/" ;
    }
    private void current(Model model){
        if(userService.getCurrentUser().isEmpty())
            return ;
        model.addAttribute("current",userService.getCurrentUser().get().getUsername());
    }
    @GetMapping("/create-user")
    public String getCreate(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user",user);
        return "createUser";
    }
    @PostMapping("/create-user")
    public String crete(@ModelAttribute("user") UserDto user, BindingResult bindingResult){
        if((user.getUsername().isBlank() || user.getUsername().isEmpty() || user.getUsername()==null)
                && (user.getPassword()==null || user.getPassword().isBlank() || user.getPassword().isEmpty())){
            bindingResult.rejectValue("username","error.create","Username must be entered!");
            bindingResult.rejectValue("password", "error.create", "Password must be entered!");
            return "createUser";
        }
        if(user.getUsername().isBlank() || user.getUsername()==null || user.getUsername().isEmpty()){
            bindingResult.rejectValue("username","error.create","Username must be entered!");
            return "createUser";
        }
        if(user.getPassword()==null || user.getPassword().isBlank() || user.getPassword().isEmpty()) {
            bindingResult.rejectValue("password", "error.create", "Password must be entered!");
            return "createUser";
        }

        var userOpt = userService.create(user);
        if(userOpt.isEmpty()) {
            bindingResult.rejectValue("username","error.create","Username is already taken!");
            return "createUser";
        }

        var userDto = userOpt.get();
        authorizationService.create(user.getUsername(), user.getPassword());
        postService.setCurrent(userDto.getUsername());
        userService.setCurrentUser(userDto.getUsername());
        return "redirect:/";
    }
    @GetMapping("/login")
    public String getLogin(Model model){
        model.addAttribute("authorization",new AuthorizationDto());
        return "index";
    }
    @PostMapping("/login")
    public String login(Model model, @ModelAttribute("authorization") AuthorizationDto authorization, BindingResult bindingResult){
        try {
            var isCorrect = authorizationService.isCorrect(authorization.getUsername(),authorization.getPassword());
            if(!isCorrect) {
                bindingResult.rejectValue("password", "error.login", "Password is wrong!");
                return "index";
            }

        }catch (UserNotExistException e){
            bindingResult.rejectValue("username","error.login","User doesn`t exist");
            return "index";
        }
        userService.setCurrentUser(authorization.getUsername());
        postService.setCurrent(authorization.getUsername());
        current(model);
        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout(Model model){
        userService.setCurrentUser(null);
        return "redirect:/login";
    }
}

