package org.mat.nounou.util;

import java.text.SimpleDateFormat;

/**
 * Gather all reusable constants of the application
 * User: mlecoutre
 * Date: 31/10/12
 * Time: 10:18
 */
public interface Constants {

    public Integer MAX_RESULT = 200;

    //TODO SHOULD use Browser local, and avoid using SDF (not threadsafe), check JODATIME or FastDateFormat (commons-lang)
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm";
    public static final SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
    public static final SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_PATTERN);
    public static final SimpleDateFormat sdfTime = new SimpleDateFormat(TIME_PATTERN);

}
