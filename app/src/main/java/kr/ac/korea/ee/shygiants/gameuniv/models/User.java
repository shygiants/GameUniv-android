package kr.ac.korea.ee.shygiants.gameuniv.models;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public class User extends Response {

    private String _id;
    private String userName;
    private String email;
    private ArrayList<Game> havePlayed;

    private String authToken;

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

//    public ArrayList getPlayedGames() {
//        return havePlayed;
//    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
