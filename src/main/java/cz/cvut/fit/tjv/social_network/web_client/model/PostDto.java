package cz.cvut.fit.tjv.social_network.web_client.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PostDto {
    private PostKeyDto key;
    private LocalDateTime added;
    private String text;
    private String image;
    private Collection<UserDto> likes = new HashSet<>();
    public PostDto() {
    }

    public PostDto(PostKeyDto key, String image, LocalDateTime added, Set<UserDto> likes, String text) {
        this.key = key;
        this.image = image;
        this.added = added;
        this.likes = likes;
        this.text = text;
    }

    public PostKeyDto getKey() {
        return key;
    }

    public void setKey(PostKeyDto key) {
        this.key = key;
    }

    public LocalDateTime getAdded() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added = added;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Collection<UserDto> getLikes() {
        return likes;
    }

    public void setLikes(Collection<UserDto> likes) {
        this.likes = likes;
    }


    @Override
    public String toString() {
        return "PostDto{" +
                "key=" + key +
                ", added=" + added +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", likes=" + likes +
                '}';
    }
}
