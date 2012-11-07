package org.mat.nounou.services;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class TimeService {

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

        hours = hours + days * 24;
        StringBuilder sb = new StringBuilder(64);

        sb.append(hours);
        sb.append(":");
        sb.append(minutes);
        return (sb.toString());
    }

    public static Double getDecimalDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        hours = hours + days * 24;
        StringBuilder sb = new StringBuilder(64);
        double min = new Double(minutes) / 60f;
        double d = new Double(hours) + min;
        return round2(d);
    }

    public static Double getDecimalTime(Date d) {
        DateTime dt = new DateTime(d);
        System.out.println(dt);
        Double h = new Double(dt.get(DateTimeFieldType.hourOfDay()));
        System.out.println("Hours: "+h);
        Double mn = new Double(dt.get(DateTimeFieldType.minuteOfHour()));
        mn = mn / 60f;
        double r =  h+mn;
        return round2(r);
    }

    public static double round2(double d) {
        d *= 100;
        d = Math.round(d);
        d /= 100;
        return d;
    }

}
