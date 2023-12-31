package cz.cvut.fit.tjv.social_network.web_client.model;


public class PostKeyDto {
    private UserDto author;
    private long id;
    public PostKeyDto(UserDto author) {
        this.author = author;
    }

    public PostKeyDto(UserDto author, long id) {
        this.author = author;
        this.id = id;
    }

    @Override
    public String toString() {
        return "PostKeyDto{" +
                "author=" + author +
                ", id=" + id +
                '}';
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
