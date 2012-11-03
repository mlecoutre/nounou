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
    private String format = "postgres://username:password@host:port/database_name";
    private String jdbcUrlTarget = "jdbc:postgresql://username:password@host:port/database_name";

    @Test
    public void testAnalyser() {
        HerokuURLAnalyser analyser = new HerokuURLAnalyser(format);
        System.out.println(analyser);
        System.out.println(analyser.generateJDBCUrl());
        assertTrue("We should obtain a valid jdbc URL.", jdbcUrlTarget.equals(analyser.generateJDBCUrl()));
    }
}
