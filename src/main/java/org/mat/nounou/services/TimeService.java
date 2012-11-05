package org.mat.nounou.services;

import java.util.concurrent.TimeUnit;


public class TimeService {

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

         hours = hours+ days*24;
        StringBuilder sb = new StringBuilder(64);

        sb.append(hours);
        sb.append(" H ");
        sb.append(minutes);
        sb.append(" Mn");
//        sb.append(seconds);
//        sb.append(" Seconds");

        return(sb.toString());
    }

}
