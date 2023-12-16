package cz.cvut.fit.tjv.social_network.web_client.service;

import cz.cvut.fit.tjv.social_network.web_client.api.UserClient;
import cz.cvut.fit.tjv.social_network.web_client.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    private final UserClient userClient;
    private String currentUser;


    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public Collection<UserDto> readUserById(){
        return userClient.getUserById();
    }
    public void create(UserDto userDto){
        userClient.create(userDto);
    }

    public boolean isCurrentUser(){
        return currentUser!=null;
    }

    private void setCurrentUser(String username){
        this.currentUser=username;
        userClient.setCurrentUserRestClient(username);
    }

    private Optional<UserDto> getCurrentUser(){
        return userClient.readCurrent();
    }
}
