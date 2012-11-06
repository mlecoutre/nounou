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
import org.mat.nounou.services.ChildrenService;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.ChildVO;

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
public class ChildrenServiceTest {

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
        ChildrenService service = new ChildrenService();
        List<ChildVO> vos = service.get();
        assertTrue("Should have a least one child in the db.", vos.size() > 0);
    }

    @Test
    public void testDeleteById() {
        ChildrenService service = new ChildrenService();
        Response r = service.deleteById(2);
        assertTrue("Response should be 200", r.getStatus() == 200);
    }


    @Test
    public void testFindByAccountId() {
        ChildrenService service = new ChildrenService();
        List<ChildVO> children = service.findByAccountId(1);
        assertTrue("First account has one child", children.size() == 1);
    }

    @Test
    public void testRegisterKid() {
        ChildrenService service = new ChildrenService();
        ChildVO vo = new ChildVO();
        vo.setNurseId(1);
        vo.setAccountId(2);
        vo.setBirthday("22/01/2013");
        vo.setFirstName("Azrael");
        vo.setLastName("L");
        vo = service.registerKid(vo);
        assertTrue("Child should be correctly inserted into the db", vo.getChildId() != null);
    }
}
