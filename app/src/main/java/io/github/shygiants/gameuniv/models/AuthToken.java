package io.github.shygiants.gameuniv.models;

/**
 * Created by SHYBook_Air on 15. 11. 9..
 */
public class AuthToken extends Response {

    private String auth_token;
    // TODO: Make use of expires_in
    private String expires_in;

    @Override
    public String toString() {
        return auth_token;
    }
}
