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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class PostController {

    private PostService postService;
    private UserService userService;
    public PostController(PostService postService, UserService userService) {
        this.userService=userService;
        this.postService = postService;
        postService.setCurrent("test1");
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
    public String createSubmit(Model model,@ModelAttribute PostDto post, @ModelAttribute StringField coUsername,
                               @ModelAttribute CheckBox coBool, @RequestParam("file") MultipartFile file){
        current(model);
        try {
            byte[] byteImg = file.getBytes();
            post.setImage(java.util.Base64.getEncoder().encodeToString(byteImg));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        postService.setCurrent(userService.getCurrentUsername());
        var postDto = postService.create(post,coBool.isCheck(),coUsername.getUsername());
        return "redirect:/";
    }
    @GetMapping("/{author}/p/{id}")
    public String getUserPost(Model model, @PathVariable("author") String username, @PathVariable("id") Long id){
        current(model);
        var postOpt = postService.getPost(username,id);
        if(postOpt.isEmpty())
            throw new RuntimeException();
        var post = postOpt.get();
        model.addAttribute("post",post);
        boolean isLiked = postService.isLiked(username,id);
        model.addAttribute("isLiked",isLiked);
        return "userPost";
    }

    @GetMapping("/{author}/p/{id}/likes")
    public String getUsersWhoLikesPost(Model model, @PathVariable("author") String username, @PathVariable("id") Long id){
        var likes = postService.getLikes(username,id);
        current(model);
        model.addAttribute("usersText","Likes");
        model.addAttribute("users",likes);
        return "usersCollection";
    }
    @PostMapping("/{author}/p/{id}/like")
    public String like(Model model, @PathVariable("author") String username, @PathVariable("id") Long id){
        current(model);
       postService.like(username,id);
       return "redirect:/"+username+"/p/"+id.toString();
    }
    @PostMapping("/{author}/p/{id}/unlike")
    public String unlike(Model model, @PathVariable("author") String username, @PathVariable("id") Long id){
        current(model);
        postService.unlike(username,id);
        return "redirect:/"+username+"/p/"+id.toString();
    }

}
