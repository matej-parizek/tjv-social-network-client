package cz.cvut.fit.tjv.social_network.web_client.web_client;

import cz.cvut.fit.tjv.social_network.web_client.model.FollowedPosts;
import cz.cvut.fit.tjv.social_network.web_client.service.PostService;
import cz.cvut.fit.tjv.social_network.web_client.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class IndexController {
    private UserService userService;
    private PostService postService;

    public IndexController(UserService userService, PostService postService) {
        this.postService=postService;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {
        current(model);
        var posts = postService.getFollowedPosts();
        current(model);
        Set<FollowedPosts> post = new HashSet<FollowedPosts>(posts);
        model.addAttribute("posts",post);
        return "followedPosts";

    }
    @PostMapping("/{author}/{id}/like")
    public String like(Model model, @PathVariable("author") String username, @PathVariable("id") Long id){
        current(model);
        postService.like(username,id);
        return "redirect:/";
    }
    @PostMapping("/{author}/{id}/unlike")
    public String unlike(Model model, @PathVariable("author") String username, @PathVariable("id") Long id){
        current(model);
        postService.unlike(username,id);
        return "redirect:/" ;
    }
    private void current(Model model){
        if(userService.getCurrentUser().isEmpty())
            return ;
        model.addAttribute("current",userService.getCurrentUser().get().getUsername());
    }
}

