package cz.cvut.fit.tjv.social_network.web_client.model;

import java.util.ArrayList;
import java.util.Collection;

public class UserDto {
    private String username;
    private String realName;
    private String info;
    private Collection<UserDto> follower = new ArrayList<>();
    private Collection<UserDto> followed = new ArrayList<>();

    public UserDto(String username, String realName, Collection<UserDto> follower, Collection<UserDto> followed,String info) {
        this.username = username;
        this.realName = realName;
        this.info= info;
        if(follower!=null)
            this.follower = follower;
        if(followed!=null)
            this.followed = followed;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", info='" + info + '\'' +
                ", follower=" + follower +
                ", followed=" + followed +
                '}';
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Collection<UserDto> getFollower() {
        return follower;
    }

    public void setFollower(Collection<UserDto> follower) {
        this.follower = follower;
    }

    public Collection<UserDto> getFollowed() {
        return followed;
    }

    public void setFollowed(Collection<UserDto> followed) {
        this.followed = followed;
    }
}
