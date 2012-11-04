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
import org.mat.nounou.services.AppointmentService;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.AppointmentVO;
import org.mat.nounou.vo.ReportVO;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: mlecoutre
 * Date: 03/11/12
 * Time: 10:46
 */
public class AppointmentServiceTest {

    private static final String DBUNIT_FILE = "/dataset.xml";

    private static IDatabaseConnection dbUnitConn;
    private EntityManagerLoaderListener listener = new EntityManagerLoaderListener();


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
    public void testGetLastAppointments() {
        AppointmentService apps = new AppointmentService();
        ReportVO report = apps.getLastAppointments(1, "last");
        assertTrue("We should have one apps", report.getAppointments().size() == 1);
        System.out.println("Total duration: " + report.getTotalDuration());
        assertTrue("", report.getTotalDuration() != null);
    }

    @Test
    public void testGetByAppointmentId() {
        AppointmentService apps = new AppointmentService();
        AppointmentVO vo = apps.getByAppointmentId(1);
        assertNotNull("Appointment with id 1 should exist", vo);
    }

    @Test
    public void testGetCurrentAppointment() {
        AppointmentService apps = new AppointmentService();
        AppointmentVO vo = apps.getCurrentAppointment(1, 1);
        assertTrue("We should have one apps", vo.getCurrentUserName().equals("Richard Wright"));
    }

    @Test
    public void testRegisterAppointment() {
        AppointmentService apps = new AppointmentService();
        AppointmentVO vo = new AppointmentVO();
        vo.setAccountId(1);
        vo.setCurrentUserId(1);
        vo.setKidId(1);
        vo.setArrivalDate("11-10-2012 08:00");
        vo.setDepartureDate("11-10-2012 19:00");
        vo.setDeclarationType("both");
        vo = apps.registerAppointment(vo);
        assertTrue("AppointmentId should not be null", vo.getAppointmentId() != null);
    }

    @Test
    public void testListAppointments() {
        AppointmentService apps = new AppointmentService();
        List<AppointmentVO> appointmentVOLists = apps.get();
        assertTrue("We should have several apps", appointmentVOLists.size() > 0);

    }

    @Test
    public void testDeleteById() {
        AppointmentService apps = new AppointmentService();
        apps.deleteById(1);
        assertTrue("Appointment should be deleted", true);

    }
}
