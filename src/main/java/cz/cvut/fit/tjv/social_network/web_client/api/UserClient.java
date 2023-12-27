package cz.cvut.fit.tjv.social_network.web_client.api;

import cz.cvut.fit.tjv.social_network.web_client.model.UserDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * ZDE se napise jak bude pracovat s api s userem
 */

@Component

public class UserClient {
    private final String base;
    private final RestClient userRestClient;
    private RestClient currentUserRestClient;

    public UserClient(@Value("${api.url}") String url){
        base=url;
        userRestClient= RestClient.create(url + "/user");
    }
    public void setCurrentUserRestClient(String username){
        currentUserRestClient= RestClient.builder()
                .baseUrl(base + "/user/{id}")
                .defaultUriVariables(Map.of("id",username))
                .build();
    }
    public Optional<UserDto> readCurrent(){
        try {
            return Optional.ofNullable(
                    currentUserRestClient.get()
                            .retrieve()
                            .toEntity(UserDto.class)
                            .getBody()
            );
        }catch (HttpClientErrorException.NotFound e){
            return Optional.empty();
        }
    }
    private String getCurrentUsername(){
        var currentUserOpt = this.readCurrent();
        if(currentUserOpt.isEmpty())
            throw new HttpClientErrorException(HttpStatusCode.valueOf(409));
        return currentUserOpt.get().getUsername();
    }
    //from API documentation
    public Optional<UserDto> readUserById(String id){
        try {
            return Optional.ofNullable(userRestClient
                            .get()
                            .uri("/{id}",Map.of("id",id))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .toEntity(UserDto.class)
                            .getBody()
            );
        }
        catch (HttpClientErrorException.NotFound e){
            return Optional.empty();
        }
    }
    public Optional<UserDto> update(UserDto userDto){
        try {
            return Optional.ofNullable(userRestClient.put()
                            .uri("/{id}",Map.of("id",getCurrentUsername()))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(userDto)
                            .retrieve()
                            .toEntity(UserDto.class)
                            .getBody());
        }catch (HttpClientErrorException.Conflict e){
            return Optional.empty();
        }

    }
    public void delete(String username){
        try{
            userRestClient.delete()
                    .uri("{id}",Map.of("id",username))
                    .retrieve()
                    .toBodilessEntity();
        }catch (HttpClientErrorException.NotFound e) {
        }
    }
    public Collection<UserDto> getFollow(String username){
        try {
            return Arrays.asList(
                    userRestClient.get()
                            .uri("/{id}/follow", Map.of("id", username))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .toEntity(UserDto[].class)
                            .getBody()
            );
        }catch (HttpClientErrorException.NotFound e) {
            return new ArrayList<>();
        }
    }
    //todo
    public void follow(String username){
        try {
            currentUserRestClient.put()
                    .uri(
                            uriBuilder -> uriBuilder
                                    .path("/follow")
                                    .queryParam("follow",username)
                                    .build()
                    )
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        }
        catch (HttpClientErrorException.NotFound e){
        }catch (HttpClientErrorException.Conflict e){
        }
    }
    //todo
    public void unfollow(String username){
        try {
            userRestClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/follow")
                            .queryParam("follow",username)
                            .build()
                    )
                    .retrieve()
                    .toBodilessEntity();
        }catch (HttpClientErrorException.NotFound e){
        }catch (HttpClientErrorException.Conflict e){
        }
    }
    public void create(UserDto userDto){
        userRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDto)
                .retrieve()
                .toBodilessEntity();
    }
    public long sumCoCreateLikes(String username){
        try {
            return userRestClient.get()
                    .uri("/{id}/sum-co-creator-likes", Map.of("id", username))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Long.class)
                    .getBody().longValue();
        }catch (HttpClientErrorException.NotFound e){
            return 0;
        }
    }
    public long sumPostLikes(String username){
        try {
            return userRestClient.get()
                    .uri("/{id}/sum-post-likes", Map.of("id", username))
                    .retrieve()
                    .toEntity(Long.class)
                    .getBody().longValue();
        }catch (HttpClientErrorException.NotFound e){
            return 0;
        }
    }
    public Collection<UserDto> getFriends(String username){
        try{
            return Arrays.asList(
                    userRestClient.get()
                            .uri("/{id}/friends", Map.of("id",username))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .toEntity(UserDto[].class)
                            .getBody()
            );
        }catch (HttpClientErrorException.NotFound e){
            return new ArrayList<>();
        }
    }
    public Collection<UserDto> getFollowers(String username){
        try{
            return Arrays.asList(
                    userRestClient.get()
                            .uri("/{id}/followers", Map.of("id",username))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .toEntity(UserDto[].class)
                            .getBody()
            );
        }catch (HttpClientErrorException.NotFound e){
            return new ArrayList<>();
        }
    }

}
