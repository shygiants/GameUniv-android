package kr.ac.korea.ee.shygiants.gameuniv.models;

/**
 * Created by SHYBook_Air on 2015. 12. 10..
 */
public class Achievement extends Response {

    private String _id;
    private String title;
    private String description;
    private int point;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPoint() {
        return point + "pt";
    }

}
