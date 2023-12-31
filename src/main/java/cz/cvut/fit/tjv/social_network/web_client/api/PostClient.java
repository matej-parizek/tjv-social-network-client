package cz.cvut.fit.tjv.social_network.web_client.api;

import cz.cvut.fit.tjv.social_network.web_client.model.PostDto;
import cz.cvut.fit.tjv.social_network.web_client.model.PostKeyDto;
import cz.cvut.fit.tjv.social_network.web_client.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.websocket.reactive.JettyWebSocketReactiveWebServerCustomizer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class PostClient {
    private String baseUrl;
    private RestClient postClient;
    private RestClient currentPostClient;

    /**
     * in every method must be set username
     * @param url is String
     */
    public PostClient(@Value("${api.url}") String url) {
        this.baseUrl = url;
        this.postClient=RestClient.builder()
                .baseUrl(
                baseUrl+"/user/{author}/posts").build();
    }
    public void setCurrentUserRestClient(String username){
        currentPostClient= RestClient.builder()
                .baseUrl(baseUrl + "/user/{author}/posts")
                .defaultUriVariables(Map.of("author",username))
                .build();
    }
    //API
    public Collection<UserDto> getLikes(String username, Long id){
        try {
            return List.of(postClient.get()
                    .uri("/likes", Map.of("author", username, "id", id))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(UserDto[].class)
                    .getBody());
        }catch (Exception e){return new ArrayList<>();}
    }
    public void likes(String username, Long id, String who){
        try {
            postClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{id}/likes")
                            .queryParam("like",who)
                            .build(Map.of("author",username,"id",id)
                            ))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        }catch (Exception e){return;}
    }
    public void unlikes(String username, Long id, String who){
        try {
            postClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{id}/likes")
                            .queryParam("like",who)
                            .build(Map.of("author",username,"id",id)
                            ))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        }catch (Exception e){return;}
    }
    public Collection<PostDto> getUserAllPost(String username){
        try {
            return List.of(postClient.get()
                    .uri("", Map.of("author", username))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(PostDto[].class)
                    .getBody());
        }catch (Exception e){return new ArrayList<>();}
    }
    public Optional<PostDto> create(PostDto post){
        try {
            return Optional.of(currentPostClient.post()
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(post)
                    .retrieve()
                    .toEntity(PostDto.class)
                    .getBody());
        }catch (Exception e){ return Optional.empty();}
    }
    public void coCreate(String coUsername, UserDto author, Long id){
       try {
           currentPostClient.post()
                   .uri(
                           uriBuilder -> uriBuilder
                                   .path("/{id}/co-create")
                                   .queryParam("coAuthor",coUsername)
                                   .build(Map.of("id",id))
                   )
                   .accept(MediaType.APPLICATION_JSON)
                   .contentType(MediaType.APPLICATION_JSON)
                   .retrieve()
                   .toBodilessEntity();
       }catch (Exception e){}
    }
    public Optional<PostDto> getPost(String username, Long id){
        try{
            return Optional.ofNullable(postClient.get()
                    .uri("/{id}", Map.of("author", username, "id", id))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(PostDto.class)
                    .getBody());
        }catch (Exception e){return Optional.empty();}
    }
    public Optional<PostDto> deletePost(String username, Long id){
        try{
            return Optional.ofNullable(postClient.delete()
                    .uri("/{id}", Map.of("author", username, "id", id))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(PostDto.class)
                    .getBody());
        }catch (Exception e){return Optional.empty();}
    }
}
