package cz.cvut.fit.tjv.social_network.web_client.web_client;

import cz.cvut.fit.tjv.social_network.web_client.model.StringField;
import cz.cvut.fit.tjv.social_network.web_client.service.CommentService;
import cz.cvut.fit.tjv.social_network.web_client.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/{username}/p/{id}")
public class CommentController {
    CommentService commentService;
    UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    private void current(Model model){
        if(userService.getCurrentUser().isEmpty())
            return ;
        model.addAttribute("current",userService.getCurrentUser().get().getUsername());
    }
    @GetMapping("/comments")
    public String getComments(Model model, @PathVariable("username")String username, @PathVariable("id")Long id ){
        if(userService.getCurrentUsername()==null)
            return "redirect:/login";
        current(model);
        var comments = commentService.getComments(username,id);
        model.addAttribute("comments", comments);
        return "comments";
    }
    @PostMapping("/create-comment")
    public String create(Model model, @PathVariable("username")String username, @PathVariable("id")Long id, @ModelAttribute("text")StringField text, BindingResult bindingResult){
        if(userService.getCurrentUsername()==null)
            return "redirect:/login";
        current(model);
        if(text.getUsername().isEmpty() || text.getUsername() == null || text.getUsername().isBlank()) {
            bindingResult.rejectValue("username", "error.create", "Text cannot be empty!");
            return "createComment";
        }
        commentService.setComment(username,id,text.getUsername());
        return "redirect:/"+username+"/p/"+id;
    }
    @GetMapping("/create-comment")
    public String createComments(Model model, @PathVariable("username")String username, @PathVariable("id")Long id){
        if(userService.getCurrentUsername()==null)
            return "redirect:/login";
        current(model);
        StringField text=new StringField();
        model.addAttribute("text",text );
        model.addAttribute("author",username);
        model.addAttribute("id",id);
        return "createComment";
    }

}
