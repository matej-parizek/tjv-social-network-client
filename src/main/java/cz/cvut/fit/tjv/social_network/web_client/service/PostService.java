package cz.cvut.fit.tjv.social_network.web_client.service;

import cz.cvut.fit.tjv.social_network.web_client.api.CommentClient;
import cz.cvut.fit.tjv.social_network.web_client.api.PostClient;
import cz.cvut.fit.tjv.social_network.web_client.api.UserClient;
import cz.cvut.fit.tjv.social_network.web_client.model.FollowedPosts;
import cz.cvut.fit.tjv.social_network.web_client.model.PostDto;
import cz.cvut.fit.tjv.social_network.web_client.model.PostKeyDto;
import cz.cvut.fit.tjv.social_network.web_client.model.UserDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class PostService {
    private PostClient postClient;
    private UserClient userClient;
    private CommentClient commentClient;
    String currentUser;

    public PostService(PostClient postClient, UserClient userClient, CommentClient commentClient) {
        this.postClient = postClient;
        this.userClient = userClient;
        this.commentClient = commentClient;
    }

    public void setCurrent(String username){
        currentUser=username;
        postClient.setCurrentUserRestClient(username);
    }
    public Collection<PostDto> getAllUserPosts(String username){
        return postClient.getUserAllPost(username);
    }
    public Optional<PostDto> create(PostDto postDto, boolean isCo, String coUsername ){
        var currentOpt = userClient.readCurrent();
        if(currentOpt.isEmpty()){
            return Optional.empty();
        }
        var current = currentOpt.get();
        long id =0; //(long) postClient.getUserAllPost(currentUser).size();
        postDto.setAdded(LocalDateTime.now());
        postDto.setKey(new PostKeyDto(current,id));
        var post =postClient.create(postDto);
        if(isCo)
            postClient.coCreate(coUsername, postDto.getKey().getAuthor(), postDto.getKey().getId());
        return post;
    }

    public Optional<PostDto> getPost(String username, Long id){
        return postClient.getPost(username,id);
    }
    public Collection<UserDto> getLikes(String username, Long id){
        return postClient.getLikes(username,id);
    }
    public boolean isLiked(String username,Long id) {
        if(currentUser.equals(username))
            return false;
        return postClient.getLikes(username, id).stream() .anyMatch(userDto -> userDto.getUsername().equals(currentUser));
    }
    public void like(String username, Long id){
        postClient.likes(username,id,currentUser);
    }
    public void unlike(String username, Long id){
        postClient.unlikes(username,id,currentUser);
    }
    public Collection<FollowedPosts> getFollowedPosts(){
        Collection<FollowedPosts> followedPosts = new ArrayList<>();
        var posts = postClient.getFollowedPost();
        for (var post: posts){
            boolean isLiked = this.isLiked(post.getKey().getAuthor().getUsername(),post.getKey().getId());
            Long sizeComments = (long)commentClient.getComments(post.getKey().getAuthor().getUsername(),post.getKey().getId()).size();
            followedPosts.add(new FollowedPosts(post,isLiked,sizeComments));
        }
        return followedPosts;
    }

    public Long commentsSize(String username, Long id){
        return (long) commentClient.getComments(username, id).size();
    }
}
