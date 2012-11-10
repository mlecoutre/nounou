package org.mat.nounou;

import org.junit.Test;
import org.mat.nounou.util.HerokuURLAnalyser;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: mlecoutre
 * Date: 03/11/12
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class HerokuURLAnalyserTest {
    String formatH2= "jdbc:h2:~/test.db";
    private String format = "postgres://username:password@host:port/database_name";
    private String jdbcUrlTarget = "jdbc:postgresql://host:port/database_name?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

    @Test
    public void testAnalyser() {
        HerokuURLAnalyser analyser = new HerokuURLAnalyser(format);
        System.out.println(analyser);
        System.out.println(analyser.getJdbcURL());
        assertTrue(analyser.getDbVendor().equals("postgres"));
        assertTrue("We should obtain a valid jdbc URL.", jdbcUrlTarget.equals(analyser.getJdbcURL()));

    }

    @Test
    public void testH2Analyser() {
        HerokuURLAnalyser analyser = new HerokuURLAnalyser(formatH2);
        System.out.println(analyser);
        System.out.println(analyser.getJdbcURL());
        assertTrue(analyser.getDbVendor().equals("h2"));
      //  assertTrue("We should obtain a valid jdbc URL.", jdbcUrlTarget.equals(analyser.generateJDBCUrl()));

    }
}
