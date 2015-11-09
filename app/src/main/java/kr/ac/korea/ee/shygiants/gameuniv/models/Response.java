package kr.ac.korea.ee.shygiants.gameuniv.models;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public abstract class Response {
    protected boolean success;
    protected String error;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
