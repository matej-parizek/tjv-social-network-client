package cz.cvut.fit.tjv.social_network.web_client.model;

public class CommentDto {
    private Long idComment;
    private UserDto author;
    private PostDto toPost;
    private String text;

    public CommentDto() {
    }

    public CommentDto(Long id, UserDto author, PostDto toPost, String text) {
        this.idComment = id;
        this.author = author;
        this.toPost = toPost;
        this.text = text;
    }

    public Long getIdComment() {
        return idComment;
    }

    public void setIdComment(Long idComment) {
        this.idComment = idComment;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public PostDto getToPost() {
        return toPost;
    }

    public void setToPost(PostDto toPost) {
        this.toPost = toPost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "idComment=" + idComment +
                ", author=" + author +
                ", toPost=" + toPost +
                ", text='" + text + '\'' +
                '}';
    }
}
