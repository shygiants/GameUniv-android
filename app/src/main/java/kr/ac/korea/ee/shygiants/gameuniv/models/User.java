package kr.ac.korea.ee.shygiants.gameuniv.models;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public class User extends Response {

    static class InnerUser {
        public String email;
        public String userName;
    }

    private InnerUser user;

    public String getEmail() {
        return user.email;
    }

    public String getUserName() {
        return user.userName;
    }
}
