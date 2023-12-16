package cz.cvut.fit.tjv.social_network.web_client.api;

import cz.cvut.fit.tjv.social_network.web_client.model.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
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

            return Optional.of(
                    currentUserRestClient.get()
                            .retrieve()
                            .toEntity(UserDto.class)
                            .getBody()
            );
        }catch (HttpClientErrorException.NotFound e){
            return Optional.empty();
        }
    }
    public Collection<UserDto>getUserById(){
        return Arrays.asList(Objects.requireNonNull(userRestClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(UserDto[].class)
                .getBody()));
    }

    public void create(UserDto userDto){
        userRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDto)
                .retrieve()
                .toBodilessEntity();
    }
}
