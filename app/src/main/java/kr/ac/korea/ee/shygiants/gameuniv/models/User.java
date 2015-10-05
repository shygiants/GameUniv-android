package kr.ac.korea.ee.shygiants.gameuniv.models;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public class User extends Response {

    private String email;
    private String userName;
    private String token;

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }
}
