package kr.ac.korea.ee.shygiants.gameuniv.models;

import kr.ac.korea.ee.shygiants.gameuniv.utils.DateFormatter;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class Moment extends Response {

    public static final int TEXT = 0;
    public static final int SCORE = 1;
    public static final int ACHIEVEMENT = 2;

    private String _id;
    private String created_at;
    private String content;
    private String type;
    private User author;
    private Game game;
    private Achievement achievement;


    public String getTimeStamp() {
        return DateFormatter.format(created_at);
    }

    public String getContent() {
        // TODO: This is temp
        if (getViewType() == ACHIEVEMENT)
            return achievement.getTitle();
        return content;
    }

    public String getType() {
        return type;
    }

    public int getViewType() {
        switch (type) {
            case "text":
                return TEXT;
            case "score":
                return SCORE;
            case "achievement":
                return ACHIEVEMENT;
            default:
                return TEXT;
        }
    }

    public User getAuthor() {
        return author;
    }

    public Game getGame() {
        return game;
    }
}
