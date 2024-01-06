package cz.cvut.fit.tjv.social_network.web_client.service;

import cz.cvut.fit.tjv.social_network.web_client.api.AuthorizationClient;
import cz.cvut.fit.tjv.social_network.web_client.exceptions.UserAlreadyExistException;
import cz.cvut.fit.tjv.social_network.web_client.exceptions.UserNotExistException;
import cz.cvut.fit.tjv.social_network.web_client.model.AuthorizationDto;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    public boolean isCorrect(String username, String password)throws UserNotExistException {
        return authorizationClient.isPasswordCorrect(username,password);
    }
    public void create(String username, String password)throws UserAlreadyExistException {
        authorizationClient.create(new AuthorizationDto(username,password));
    }

}
