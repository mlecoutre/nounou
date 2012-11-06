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
import org.mat.nounou.services.NurseService;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.NurseVO;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: mlecoutre
 * Date: 03/11/12
 * Time: 14:51
 */
public class NurseServiceTest {

    private static final String DBUNIT_FILE = "/dataset.xml";

    private static IDatabaseConnection dbUnitConn;
    private EntityManagerLoaderListener listener = new EntityManagerLoaderListener(false);

    @Test
    public void testGet() {
        NurseService service = new NurseService();
        List<NurseVO> vos = service.get();
        assertTrue("Nurse list should not be null", vos.size() > 0);
    }

    @Test
    public void testRegisterNurse() {
        NurseService service = new NurseService();
        NurseVO vo = new NurseVO();
        vo.setFirstName("nurseFirstName");
        vo.setLastName("nurseLastName");
        vo.setPhoneNumber("9877687677");
        vo.setAccountId(1);

        vo = service.registerNurse(vo);
        assertTrue("A nurseId should be set.", vo.getNurseId() != null);

    }

    @Test
    public void testFindByAccountId() {
        NurseService service = new NurseService();
        List<NurseVO> vos = service.findByAccountId(1);
        assertTrue("The account with accountId=1 has two nurses.", vos.size() == 2);

    }

    @Test
    public void testDeleteById() {
        NurseService service = new NurseService();
        Response r = service.deleteById(2);
        System.out.println(r.getStatus());
    }


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

}
