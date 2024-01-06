package cz.cvut.fit.tjv.social_network.web_client.api;

import cz.cvut.fit.tjv.social_network.web_client.exceptions.UserAlreadyExistException;
import cz.cvut.fit.tjv.social_network.web_client.exceptions.UserNotExistException;
import cz.cvut.fit.tjv.social_network.web_client.model.AuthorizationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class AuthorizationClient {
    private final RestClient authorizationClient;

    public AuthorizationClient(@Value("${api.url}") String url) {
        this.authorizationClient= RestClient.builder()
                .baseUrl(url+"/authorization")
                .build();
    }

    public void create(AuthorizationDto authorization){
        try {
            authorizationClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(authorization)
                    .retrieve()
                    .toBodilessEntity();
        }catch (HttpClientErrorException.Conflict e){
            throw new UserAlreadyExistException();
        }
    }

    public Boolean isPasswordCorrect(String username, String password){
        try {
            return authorizationClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{username}")
                            .queryParam("password",password)
                            .build(Map.of("username",username))
                    )
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Boolean.class)
                    .getBody();
        }catch (HttpClientErrorException.Conflict e){
            return false;
        }catch (HttpClientErrorException.NotFound e){
            throw new UserNotExistException();
        }
    }
}
