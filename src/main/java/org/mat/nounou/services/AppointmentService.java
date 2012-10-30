package org.mat.nounou.services;

import org.mat.nounou.model.Appointment;
import org.mat.nounou.model.Child;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.AppointmentVO;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 28/10/12
 * Time: 11:13
 */
@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentService {

    public static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);

    @GET
    public List<Appointment> get() {
        System.out.println("Appointment service");
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("FROM Appointment", Appointment.class);
        query.setMaxResults(200);
        List<Appointment> appointments = query.getResultList();
        return appointments;
    }


    @GET
    @Path("/user/{userId}/searchType/{searchType}")
    public List<AppointmentVO> getLastAppointments(@PathParam("userId") Integer userId, @PathParam("searchType") String searchType) {
        System.out.println("getLastAppointments service");
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("FROM Appointment WHERE arrivalUser.userId=:userId", Appointment.class);
        query.setParameter("userId", userId);
        if ("last".equals(searchType)) {
            query.setMaxResults(5);
        } else {
            query.setMaxResults(31);
        }
        List<Appointment> appointments = query.getResultList();
        List<AppointmentVO> vos = new ArrayList<AppointmentVO>();
        for (Appointment app : appointments) {
            AppointmentVO vo = new AppointmentVO();
            vo.setExistingAppointmentId(app.getAppointmentId());
            vo.setDepartureUserName(app.getDepartureUser().getFirstName().concat(" ").concat(app.getDepartureUser().getLastName()));
            if (app.getDepartureDate() != null)
                vo.setDepartureDate(sdf.format(app.getDepartureDate()));
            if (app.getArrivalDate() != null)
                vo.setArrivalDate(sdf.format(app.getArrivalDate()));
            vo.setArrivalUserName(app.getArrivalUser().getFirstName().concat(" ").concat(app.getArrivalUser().getLastName()));
            vo.setKidId(1);
            vo.setKidName("Amael");
            vos.add(vo);
        }
        return vos;
    }

    /**
     * Used for initialization of run  part
     *
     * @param userId userId
     * @return AppointmentVO
     */
    @GET
    @Path("/current/{userId}")
    public AppointmentVO getCurrentAppointment(@PathParam("userId") Integer userId) {

        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<User> qUser = em.createQuery("FROM User c WHERE userId=:userId", User.class);
        qUser.setParameter("userId", userId);
        User u = qUser.getSingleResult();
        AppointmentVO vo = new AppointmentVO();
        vo.setUserId(userId);
        //TODO manage kid  other than 1
        vo.setKidId(1);
        vo.setKidName("Amael");

        vo.setCurrentUserName(u.getFirstName().concat(" ").concat(u.getLastName()));
        //TODO check if we have an existing appointment today in db

        //TODO populate AppointmentVo wit app
        Date currentDate = new Date();

        String dateStr = sdf.format(currentDate);
        if (Calendar.HOUR_OF_DAY > 12) {
            //we consider that is the arrival
            vo.setArrivalDate(dateStr);
            vo.setDeclarationType("departure");
        } else {
            //we consider that is the arrival
            vo.setDepartureDate(dateStr);
            vo.setDeclarationType("arrival");
        }

        return vo;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AppointmentVO registerAppointment(AppointmentVO appointment) {
        System.out.println("register appointment " + appointment);

        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            Appointment entity = null;
            if (appointment.getExistingAppointmentId() != null) {
                //TODO get Existing appointment in db
                TypedQuery<Appointment> qApp = em.createQuery("FROM Appointment a WHERE a.accountUser.userId=:userId AND a.arrivalDate EQUALS CURRENT_DATE", Appointment.class);
                qApp.setParameter("userId", appointment.getUserId());
                Appointment app = qApp.getSingleResult();


            } else {
                entity = new Appointment();
            }


            //TODO update to attach children to other nurse and accountUser than 1
            TypedQuery<User> qUser = em.createQuery("FROM User c WHERE userId=:userId", User.class);
            qUser.setParameter("userId", appointment.getUserId());
            User u = qUser.getSingleResult();
            Date d = null;
            if ("departure".equals(appointment.getDeclarationType())) {
                entity.setDepartureUser(u);
                d = sdf.parse(appointment.getDepartureDate());
                entity.setDepartureDate(d);
            } else if ("arrival".equals(appointment.getDeclarationType())) {
                entity.setArrivalUser(u);
                d = sdf.parse(appointment.getArrivalDate());
                entity.setArrivalDate(d);
            } else if ("both".equals(appointment.getDeclarationType())) {
                entity.setDepartureUser(u);
                entity.setArrivalUser(u);
                d = sdf.parse(appointment.getArrivalDate());
                entity.setArrivalDate(d);
                d = sdf.parse(appointment.getDepartureDate());
                entity.setDepartureDate(d);
            } else {
                //todo manage error
            }
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointment;
    }

    @GET
    @Path("/delete/{appointmentId}")
    public void deleteById(@PathParam("appointmentId") Integer appointmentId) {
        List<Child> c = null;
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Appointment WHERE appointmentId=:appointmentId");

            query.setParameter("appointmentId", appointmentId);
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
