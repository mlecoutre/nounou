package org.mat.nounou;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mat.nounou.model.Time;
import org.mat.nounou.services.TimeService;
import org.mat.nounou.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimeTest {

    @Test
    public void testDefaultTimezone() throws Exception {
        final Time time = new Time();
        assertEquals(TimeZone.getDefault().getDisplayName(), time.getTimezone());
    }

    @Test
    public void testSpecifiedTimezone() throws Exception {
        final TimeZone est = TimeZone.getTimeZone("EST");
        final Time time = new Time(est);
        assertEquals(est.getDisplayName(), time.getTimezone());
    }

    @Test
    public void testIfItsTheMorning() {
        System.out.println(">>" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        assertTrue(Calendar.HOUR_OF_DAY < 12);
    }

    @Test
    public void testGetDecimalDurationBreakdown() throws Exception {
        DateTime dt = new DateTime();
        Date end = dt.toDate();
        dt = dt.minusHours(8);
        dt = dt.minusMinutes(25);
        Date start = dt.toDate();
        Double d = TimeService.getDecimalDurationBreakdown(end.getTime() - start.getTime());
        System.out.println(start + "/" + end + ":=" + d);
        assertTrue(d == 8.42);

    }

    @Test
    public void testGetDecimalTime()throws Exception{
        Date d = Constants.sdf.parse("11-11-2012 08:25");
        double dd =TimeService.getDecimalTime(d);
        System.out.printf("testGetDecimalTime for 08:25: %2f\n", dd);
        assertTrue(dd == 8.42);
    }
}
