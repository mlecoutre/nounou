package org.mat.nounou.util;

import java.util.StringTokenizer;

/**
 * Allow to generate a proper JDBC URL (only for postgresl at this time)
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


    public HerokuURLAnalyser(String herokuURL) {
        analyse(herokuURL);

    }

    /**
     * Within the heroku url, allow to get the JDBC URL
     *
     * @return valid JDBC URL
     */
    public String generateJDBCUrl() {
        return String.format("jdbc:postgresql://%s:%s@%s:%s/%s", userName, password, host, port, databaseName);

    }

    private void analyse(String herokuURL) {

        //   herokuURL = herokuURL.substring(11, herokuURL.length());
        StringTokenizer st = new StringTokenizer(herokuURL, ":@/");
        String bd = st.nextToken();
        userName = st.nextToken();
        password = st.nextToken();
        host = st.nextToken();
        port = st.nextToken();
        databaseName = st.nextToken();
    }

    @Override
    public String toString() {
        return "HerokuURLAnalyser{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", databaseName='" + databaseName + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
