package org.mat.nounou.util;

import java.util.StringTokenizer;

/**
 * Allow to generate a proper JDBC UR. Transform only for postgresl at this time.
 * <p/>
 * User: mlecoutre
 * Date: 03/11/12
 * Time: 14:13
 */
public class HerokuURLAnalyser {
    // String format = "postgres://username:password@host:port/database_name";

    private String userName;
    private String password;
    private String host;
    private String port;
    private String databaseName;
    private String dbVendor;
    private String jdbcUrl;


    public HerokuURLAnalyser(String herokuURL) {
        analyse(herokuURL);

    }

    /**
     * Within the heroku url, allow to get the JDBC URL
     *
     * @return valid JDBC URL
     */
    public String getJdbcURL() {
        return jdbcUrl;

    }


    private void analyse(String databaseUrl) {
        StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
        dbVendor = st.nextToken(); //if DATABASE_URL is set
        if ("postgres".equals(dbVendor)) {
            userName = st.nextToken();
            password = st.nextToken();
            host = st.nextToken();
            port = st.nextToken();
            databaseName = st.nextToken();
            jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", host, port, databaseName);
        } else {
            //we consider the URL as valid jdbc
            dbVendor = st.nextToken();
            jdbcUrl = databaseUrl;
            userName = "sa";
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDbVendor() {
        return dbVendor;
    }

    @Override
    public String toString() {
        return "HerokuURLAnalyser{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", dbVendor='" + dbVendor + '\'' +
                '}';
    }
}
