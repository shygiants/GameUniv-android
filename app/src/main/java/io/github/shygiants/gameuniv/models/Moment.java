package io.github.shygiants.gameuniv.models;

import android.widget.ImageView;

import io.github.shygiants.gameuniv.utils.DateFormatter;
import io.github.shygiants.gameuniv.utils.ImageHandler;
import io.github.shygiants.gameuniv.utils.RESTAPI;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class Moment extends Response {

    public static final int TEXT = 0;
    public static final int SCORE = 1;
    public static final int ACHIEVEMENT = 2;
    public static final int IMAGE = 3;

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
            case "image":
                return IMAGE;
            default:
                return TEXT;
        }
    }

    public void getImage(ImageView imageView) {
        ImageHandler.getInstance().load(RESTAPI.getURL() + "/moments/" + _id + "/images").into(imageView);
    }

    public User getAuthor() {
        return author;
    }

    public Game getGame() {
        return game;
    }

    public Achievement getAchievement() {
        return achievement;
    }
}
