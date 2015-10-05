package kr.ac.korea.ee.shygiants.gameuniv.models;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public abstract class Response {
    protected boolean success;
    protected String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
