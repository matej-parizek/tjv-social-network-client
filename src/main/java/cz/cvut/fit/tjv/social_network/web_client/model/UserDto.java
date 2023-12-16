package cz.cvut.fit.tjv.social_network.web_client.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter @Setter
public class UserDto {
    private String username;
    private String realName;
    private Collection<UserDto> follower;
    private Collection<PostDto> likes;
}
