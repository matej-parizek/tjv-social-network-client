package cz.cvut.fit.tjv.social_network.web_client.model;

public class AuthorizationDto {
    private String username;
    private String password;

    public AuthorizationDto() {
    }

    public AuthorizationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthorizationDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
