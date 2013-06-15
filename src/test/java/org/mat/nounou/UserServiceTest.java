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
import org.mat.nounou.services.UserService;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.UserVO;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: mlecoutre
 * Date: 03/11/12
 * Time: 15:14
 */
public class UserServiceTest {

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
    public void testGet() {
        UserService service = new UserService();
        List<UserVO> vos = service.get();
        assertTrue("Should have several users in db.", vos.size() > 1);
    }

    @Test
    public void testDeleteById() {
        UserService service = new UserService();
        Response r = service.deleteById(4);
        assertTrue(r.getStatus() == 200);
    }

    @Test
    public void testFindByAccountId() {
        UserService service = new UserService();
        List<UserVO> vos = service.findByAccountId(0, 1);
        assertTrue("Should have at least one user in db with accoundId=1.", vos.size() > 0);
    }

    @Test
    public void testRegisterUser() {
        //Create a new user and a new account
        UserService service = new UserService();
        UserVO vo = new UserVO();
        vo.setNewUser(true);
        vo.setEmail("e@mail.com");
        vo.setFirstName("userFirstname");
        vo.setLastName("userLastname");
        vo.setPassword("password");
        vo.setPhoneNumber("060987987");
        vo.setType("Father");
        Response r = service.registerUser(vo);
        vo = (UserVO) r.getEntity();
        assertTrue("A userId must be set.", vo.getUserId() != null);
    }

    @Test
    public void testFindByUserId() {
        UserService service = new UserService();
        UserVO vo = service.findByUserId(1);
        assertTrue("A UserVO should be retrieved.", vo != null);
    }
}
