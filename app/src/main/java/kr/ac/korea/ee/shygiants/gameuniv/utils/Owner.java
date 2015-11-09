package kr.ac.korea.ee.shygiants.gameuniv.utils;

/**
 * Created by SHYBook_Air on 15. 11. 10..
 */
public class Owner {
    private static Owner ourInstance = new Owner();

    private String email;
    private String token;

    public static void init(String email, String token) {
        ourInstance.email = email;
        ourInstance.token = token;
    }

    public static String getEmail() {
        return ourInstance.email;
    }

    public static String getToken() {
        return ourInstance.token;
    }

    private Owner() {
    }
}
