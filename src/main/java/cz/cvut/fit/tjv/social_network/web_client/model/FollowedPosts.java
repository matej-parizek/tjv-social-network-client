package cz.cvut.fit.tjv.social_network.web_client.model;


public class FollowedPosts {
    private final PostDto post;
    private boolean isLiked;

    public FollowedPosts(PostDto post, boolean isLiked) {
        this.post = post;
        this.isLiked = isLiked;
    }

    public PostDto getPost() {
        return post;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public String toString() {
        return "FollowedPosts{" +
                "post=" + post +
                ", isLiked=" + isLiked +
                '}';
    }
}
