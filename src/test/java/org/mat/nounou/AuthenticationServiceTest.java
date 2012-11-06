package org.mat.nounou;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mat.nounou.services.AuthenticationService;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.Token;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

/**
 * User: mlecoutre
 * Date: 03/11/12
 * Time: 15:05
 */
public class AuthenticationServiceTest {

    private static final String DBUNIT_FILE = "/dataset.xml";

    private static IDatabaseConnection dbUnitConn;
    private EntityManagerLoaderListener listener = new EntityManagerLoaderListener(false);


    /**
     * initTable
     *
     * @throws Exception on error
     */
    @Before
    public void initTable() throws Exception {

        listener.contextInitialized(null);
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {

            Session session = ((Session) em.getDelegate());
            session.doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {

                    try {

                        dbUnitConn = new DatabaseConnection(connection);

                        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
                        builder.setColumnSensing(true);
                        IDataSet dataSet = builder
                                .build(AppointmentServiceTest.class
                                        .getResourceAsStream(DBUNIT_FILE));

                        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn,
                                dataSet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            // dbUnitConn.close();
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            em.close();
        }
    }

    @After
    public void close() {
        listener.contextDestroyed(null);

    }

    @Test
    public void testValidregisterUser() {
        AuthenticationService service = new AuthenticationService();
        Token token = new Token();
        token.setUid("rwright@heroku.com");
        token.setPassword("password");
        Response r = service.registerUser(token);
        token = (Token) r.getEntity();

        assertTrue("A valid accoundId should be set.", token.getAccountId() == 1);
    }

    @Test
    public void testInvalidregisterUser() {
        AuthenticationService service = new AuthenticationService();
        Token token = new Token();
        token.setUid("invalid@user.com");
        token.setPassword("Invalidpassword");
        Response r = service.registerUser(token);


        assertTrue("Status code 500 should be returned.", r.getStatus() == 500);
    }
}
