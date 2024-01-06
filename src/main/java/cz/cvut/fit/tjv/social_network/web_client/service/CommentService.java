package cz.cvut.fit.tjv.social_network.web_client.service;

import cz.cvut.fit.tjv.social_network.web_client.api.CommentClient;
import cz.cvut.fit.tjv.social_network.web_client.api.PostClient;
import cz.cvut.fit.tjv.social_network.web_client.api.UserClient;
import cz.cvut.fit.tjv.social_network.web_client.model.CommentDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentClient commentClient;
    private final PostClient postClient;
    private final UserClient userClient;

    public CommentService(CommentClient commentClient, PostClient postClient, UserClient userClient) {
        this.commentClient = commentClient;
        this.postClient = postClient;
        this.userClient = userClient;
    }

    public Optional<CommentDto> setComment(String author, Long id, String text){
        var currentUserOpt = userClient.readCurrent();
        var postOpt = postClient.getPost(author,id);
        if(postOpt.isEmpty() || currentUserOpt.isEmpty())
            return Optional.empty();
        Long idComment = (long) 0;
        CommentDto body = new CommentDto(idComment,currentUserOpt.get(),postOpt.get(),text);
        body.setIdComment(idComment);
        return commentClient.setComments(author, id, body);
    }
    public Collection<CommentDto> getComments(String author, Long id){
        return commentClient.getComments(author, id);
    }
}
