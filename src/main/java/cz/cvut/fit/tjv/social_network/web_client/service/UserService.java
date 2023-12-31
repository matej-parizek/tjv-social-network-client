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
        setCurrentUser("test1");
    }

    public boolean isCurrentUser(){
        return currentUser!=null;
    }
    public void setCurrentUser(String username){
        this.currentUser=username;
        userClient.setCurrentUserRestClient(username);
    }
    public Optional<UserDto> getCurrentUser(){
        return userClient.readCurrent();
    }
    public Optional<UserDto> readUserById(String username){
        return userClient.readUserById(username);
    }
    public Optional<UserDto> update(UserDto userDto){
        return userClient.update(userDto);
    }
    public Collection<UserDto> getAll(){
        return userClient.getAll();
    }
    public void delete(String username){
        userClient.delete(username);
    }
    public Collection<UserDto> getFollowed(String username){
        return userClient.getFollow(username);
    }
    public void follow(String followed){
        userClient.follow(followed);
    }
    public void unfollow(String username){
        userClient.unfollow(username);
    }
    public void create(UserDto userDto){
        userClient.create(userDto);
    }
    public long sumPostLikes(String username){
        return userClient.sumPostLikes(username);
    }
    public long sumCoWorkerLikes(String username){
        return userClient.sumCoCreateLikes(username);
    }
    public long sumAllLikes(String username){
        return sumPostLikes(username)+sumCoWorkerLikes(username);
    }
    public Collection<UserDto> getFriends(String username){
        return userClient.getFriends(username);
    }
    public Collection<UserDto> getFollowers(String username){
        return userClient.getFollowers(username);
    }

    public boolean isFollowed(String username) {
        if(currentUser.equals(username))
            return false;
        return userClient.getFollow(currentUser).stream()
                .anyMatch(userDto -> userDto.getUsername().equals(username));
    }

    public String getCurrentUsername() {
        return currentUser;
    }
}
