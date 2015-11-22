package kr.ac.korea.ee.shygiants.gameuniv.models;

import kr.ac.korea.ee.shygiants.gameuniv.utils.DateFormatter;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class Moment extends Response {

    private String _id;
    private String created_at;
    private String content;
    private User author;
    private Game game;


    public String getTimeStamp() {
        return DateFormatter.format(created_at);
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public Game getGame() {
        return game;
    }
}
