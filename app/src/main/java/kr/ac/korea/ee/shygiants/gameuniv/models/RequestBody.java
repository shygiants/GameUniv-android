package kr.ac.korea.ee.shygiants.gameuniv.models;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public class RequestBody {

    // POST /users
    private String email;
    private String passwd;
    private String confirmPasswd;
    private String userName;

    // POST /authTokens/:email
//    private String email;
//    private String passwd;

    public static RequestBody create(String email, String passwd, String confirmPasswd, String userName) {
        RequestBody newBody = new RequestBody();
        newBody.email = email;
        newBody.passwd = passwd;
        newBody.confirmPasswd = confirmPasswd;
        newBody.userName = userName;

        return newBody;
    }

    public static RequestBody create(String email, String passwd) {
        RequestBody newBody = new RequestBody();
        newBody.email = email;
        newBody.passwd = passwd;

        return newBody;
    }
}
