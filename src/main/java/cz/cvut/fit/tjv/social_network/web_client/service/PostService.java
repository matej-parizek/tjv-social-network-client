package cz.cvut.fit.tjv.social_network.web_client.service;

import cz.cvut.fit.tjv.social_network.web_client.api.PostClient;
import cz.cvut.fit.tjv.social_network.web_client.api.UserClient;
import cz.cvut.fit.tjv.social_network.web_client.model.PostDto;
import cz.cvut.fit.tjv.social_network.web_client.model.PostKeyDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class PostService {
    private PostClient postClient;
    private UserClient userClient;
    String currentUser;
    public PostService( PostClient postClient, UserClient userClient) {
        this.postClient = postClient;
        this.userClient = userClient;
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
        long id = (long) postClient.getUserAllPost(currentUser).size();
        postDto.setAdded(LocalDateTime.now());
        postDto.setKey(new PostKeyDto(current,id));
        var post =postClient.create(postDto);
        if(isCo)
            postClient.coCreate(coUsername, postDto.getKey().getAuthor(), postDto.getKey().getId());

        return post;
    }

}
