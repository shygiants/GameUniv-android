package kr.ac.korea.ee.shygiants.gameuniv.utils;

import java.util.regex.Pattern;

/**
 * Created by SHYBook_Air on 15. 12. 9..
 */
public class Regex {

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmail(String string) {
        return EMAIL.matcher(string).find();
    };
}
