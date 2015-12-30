package io.github.shygiants.gameuniv.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by SHYBook_Air on 15. 11. 22..
 */
public class DateFormatter {

    private static final SimpleDateFormat serverFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateFormat dateTimeFormat =
            SimpleDateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.KOREA);
    private static final DateFormat timeFormat =
            SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.KOREA);

    private static final long MINUTE_IN_MILLIS = 60 * 1000;
    private static final long HOUR_IN_MINUTES = 60;
    private static final long DAY_IN_MINUTES = 24 * HOUR_IN_MINUTES;
    private static final long TWO_DAYS_IN_MINUTES = 2 * DAY_IN_MINUTES;

    public static String format(String dateTime) {
        try {
            serverFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar currentTime = Calendar.getInstance(Locale.KOREA);
            Calendar createdAt = new GregorianCalendar();
            createdAt.setTime(serverFormat.parse(dateTime));

            long currentTimeInMinutes = currentTime.getTimeInMillis() / MINUTE_IN_MILLIS;
            long createdAtInMinutes = createdAt.getTimeInMillis() / MINUTE_IN_MILLIS;

            long differenceInMinutes = currentTimeInMinutes - createdAtInMinutes;

            if (differenceInMinutes > TWO_DAYS_IN_MINUTES ||
                    currentTime.get(Calendar.DATE) - createdAt.get(Calendar.DATE) == 2)
                return dateTimeFormat.format(createdAt.getTime());
            else if (differenceInMinutes > DAY_IN_MINUTES)
                return "어제 " + timeFormat.format(createdAt.getTime());
            else if (differenceInMinutes > HOUR_IN_MINUTES)
                return (int)(differenceInMinutes / HOUR_IN_MINUTES) + "시간";
            else if (differenceInMinutes == 0)
                return "지금";
            else
                return differenceInMinutes + "분";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
