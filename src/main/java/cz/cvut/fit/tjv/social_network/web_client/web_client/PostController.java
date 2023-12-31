package cz.cvut.fit.tjv.social_network.web_client.web_client;

import cz.cvut.fit.tjv.social_network.web_client.model.CheckBox;
import cz.cvut.fit.tjv.social_network.web_client.model.PostDto;
import cz.cvut.fit.tjv.social_network.web_client.model.StringField;
import cz.cvut.fit.tjv.social_network.web_client.service.PostService;
import cz.cvut.fit.tjv.social_network.web_client.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("")
public class PostController {

    private PostService postService;
    private UserService userService;
    public PostController(PostService postService, UserService userService) {
        this.userService=userService;
        this.postService = postService;
    }
    private void current(Model model){
        if(userService.getCurrentUser().isEmpty())
            return ;
        model.addAttribute("current",userService.getCurrentUser().get().getUsername());
    }

    @GetMapping("/{author}/p")
    public String getUserAllPost(Model model, @PathVariable("author") String username){
        current(model);
        var post = postService.getAllUserPosts(username);
        model.addAttribute("posts",post);
        return "userPosts";
    }
    @GetMapping("/create-post")
    public String create(Model model){
        current(model);
        var post = new PostDto();

        model.addAttribute("post",post);
        StringField coUsername = new StringField();
        CheckBox coBool = new CheckBox();
        model.addAttribute("coBool",coBool);
        model.addAttribute("coUsername",coUsername);
        return "createPost";
    }
    @PostMapping("/create-post")
    public String createSubmit(Model model, @ModelAttribute PostDto post, @ModelAttribute StringField coUsername,
                               @ModelAttribute CheckBox coBool,
                               @RequestParam("file") MultipartFile file){
        try {
            byte[] bytes = file.getBytes();

            post.setImage(Base64.encodeBase64(bytes, false));
        } catch (Exception e) {

        }
        postService.setCurrent(userService.getCurrentUsername());
        var postDto = postService.create(post,coBool.isCheck(),coUsername.getUsername());
        return "index";
    }
}
