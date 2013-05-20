package org.mat.nounou.services;

import org.joda.time.DateTime;
import org.mat.nounou.model.Account;
import org.mat.nounou.model.Appointment;
import org.mat.nounou.model.Child;
import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.util.Check;
import org.mat.nounou.util.Constants;
import org.mat.nounou.vo.AppointmentVO;
import org.mat.nounou.vo.ChildVO;
import org.mat.nounou.vo.ReportVO;
import org.mat.nounou.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Value Object for Appointment
 * AppointmentVO: mlecoutre
 * Date: 28/10/12
 * Time: 11:13
 */
@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @GET
    public List<AppointmentVO> get() {
        logger.debug("Appointment service");
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("FROM Appointment", Appointment.class);
        List<AppointmentVO> apps = new ArrayList<AppointmentVO>();
        try {
            query.setMaxResults(Constants.MAX_RESULT);
            List<Appointment> appointments = query.getResultList();
            for (Appointment a : appointments) {
                AppointmentVO appVo = populateAppointmentVO(a);
                apps.add(appVo);
            }
        } catch (NoResultException nre) {
            logger.error("No appointment at this time");
        } finally {
            em.close();
        }
        return apps;
    }

    @GET
    @Path("/{appointmentId}")
    public AppointmentVO getByAppointmentId(@PathParam("appointmentId") Integer appointmentId) {
        logger.debug("Appointment service");
        if (Check.checkIsEmptyOrNull(appointmentId)) {
            logger.warn(String.format("WARNING: Incorrect parameter for getByAppointmentId:%d\n", appointmentId));
            return null;
        }
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        AppointmentVO appVo = null;
        try {
            Appointment a = em.find(Appointment.class, appointmentId);
            appVo = populateAppointmentVO(a);
        } catch (NoResultException nre) {
            logger.debug("No appointment  with id:" + appointmentId);
        } finally {
            em.close();
        }
        return appVo;
    }

    /**
     * Used for reports
     *
     * @param accountId
     * @param searchType
     * @return report
     */
    @GET
    @Path("{appointmentId}/account/{accountId}/searchType/{searchType}")
    public ReportVO getLastAppointments(@PathParam("accountId") Integer accountId, @PathParam("searchType") String searchType) {
        logger.debug("getLastAppointments service");
        List<Double[]> data = new ArrayList<Double[]>();
        List<Double[]> dataRange = new ArrayList<Double[]>();
        //Check input parameters
        if (Check.checkIsEmptyOrNull(accountId) || Check.checkIsEmptyOrNull(searchType)) {
            logger.warn(String.format("WARNING: Incorrect parameters accountId:%d, searchType:%s\n", accountId, searchType));
            return null;
        }
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        List<AppointmentVO> vos = new ArrayList<AppointmentVO>();
        long totalDuration = 0;
        try {
            StringBuffer buff = new StringBuffer("FROM Appointment WHERE accountId=:accountId");
            TypedQuery<Appointment> query = null;
            if ("last".equals(searchType)) {
                buff.append(" ORDER BY  arrivalDate DESC");
                query = em.createQuery(buff.toString(), Appointment.class);
            } else if ("currentMonth".equals(searchType)) {
                buff.append(" AND MONTH(arrivalDate) = MONTH(CURRENT_DATE) ORDER BY arrivalDate DESC");
                query = em.createQuery(buff.toString(), Appointment.class);
            } else {
                Calendar c = Calendar.getInstance();
                Date d1 = null, d2 = null;
                if ("currentWeek".equals(searchType)) {
                    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    d1 = c.getTime();
                    c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    d2 = c.getTime();
                } else if ("lastWeek".equals(searchType)) {
                    Date d = new DateTime().minusWeeks(1).toDate();
                    c.setTime(d);
                    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    d1 = c.getTime();
                    c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    d2 = c.getTime();
                } else if ("prevMonth".equals(searchType)) {
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    DateTime dt = new DateTime().minusMonths(1);
                    dt.minusDays(dt.dayOfMonth().get() - 1);
                    c.setTime(dt.toDate());
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    d1 = c.getTime();
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                    d2 = c.getTime();
                }
                buff.append(" AND  arrivalDate > :startDate AND arrivalDate < :endDate");
                buff.append(" ORDER BY arrivalDate DESC");
                query = em.createQuery(buff.toString(), Appointment.class);
                query.setParameter("startDate", d1, TemporalType.DATE);
                query.setParameter("endDate", d2, TemporalType.DATE);
            }
            query.setParameter("accountId", accountId);

            if ("last".equals(searchType)) {
                query.setMaxResults(5);
            } else {
                query.setMaxResults(31);
            }
            List<Appointment> appointments = query.getResultList();
            //populate list of appointments
            for (Appointment app : appointments) {
                AppointmentVO vo = populateAppointmentVO(app);

                //calculate duration
                if (app.getDepartureDate() != null && app.getArrivalDate() != null) {
                    long timeMilli = app.getDepartureDate().getTime() - app.getArrivalDate().getTime();
                    totalDuration = totalDuration + timeMilli;
                    vo.setDuration(TimeService.getDurationBreakdown(timeMilli));
                    //
                    Calendar c = Calendar.getInstance();
                    c.setTime(Constants.sdfDate.parse(Constants.sdfDate.format(app.getDepartureDate())));
                    // highcharts needs value from oldest to newest.
                    data.add(0, new Double[]{new Double(c.getTime().getTime()), TimeService.getDecimalDurationBreakdown(timeMilli)});
                    dataRange.add(0, new Double[]{new Double(c.getTime().getTime()), TimeService.getDecimalTime(app.getArrivalDate()), TimeService.getDecimalTime(app.getDepartureDate())});
                } else {
                    vo.setDuration("n/a");
                }
                vos.add(vo);
            }
        } catch (NoResultException nre) {
            logger.warn(String.format("ERROR: No result found with accountId:%d, searchType: %s\n", accountId, searchType));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        ReportVO reportVO = new ReportVO();
        reportVO.setAppointments(vos);
        //calculate total duration
        reportVO.setData(data);
        reportVO.setDataRange(dataRange);
        reportVO.setReportTitle("Total duration");
        reportVO.setTotalDuration(TimeService.getDurationBreakdown(totalDuration));

        return reportVO;
    }

    public static AppointmentVO populateAppointmentVO(Appointment a) {
        AppointmentVO appVo = new AppointmentVO();
        appVo.setAppointmentId(a.getAppointmentId());
        if (a.getArrivalDate() != null)
            appVo.setArrivalDate(Constants.sdf.format(a.getArrivalDate()));
        if (a.getArrivalUser() != null) {
            appVo.setArrivalUser(UserVO.populate(a.getArrivalUser()));
        }
        if (a.getDepartureDate() != null)
            appVo.setDepartureDate(Constants.sdf.format(a.getDepartureDate()));
        if (a.getDepartureUser() != null) {
            appVo.setDepartureUser(UserVO.populate(a.getDepartureUser()));
        }
        List<Integer> kidIds = new ArrayList<Integer>();
        List<ChildVO> childVOs = new ArrayList<ChildVO>();
        for (Child c : a.getChildren()) {
            kidIds.add(c.getChildId());
            ChildVO cvo = ChildrenService.populate(c);
            childVOs.add(cvo);
        }
        appVo.setKidIds(kidIds);
        appVo.setChildren(childVOs);


        //TODO map Planned date when it will be available
        return appVo;
    }

    /**
     * Used for initialization of run  part
     *
     * @param userId userId
     * @return AppointmentVO
     */
    @GET
    @Path("/{appointmentId}/account/{accountId}/user/{userId}")
    public AppointmentVO getCurrentAppointment(@PathParam("accountId") Integer accountId, @PathParam("userId") Integer userId) {
        AppointmentVO vo = new AppointmentVO();
        boolean isAppExist = false;
        //Check input parameters
        if (Check.checkIsEmptyOrNull(accountId) || Check.checkIsEmptyOrNull(userId)) {
            logger.warn(String.format("WARNING: Incorrect parameters accountId:%d, userId:%d\n", accountId, userId));
            return null;
        }
        EntityManager em = EntityManagerLoaderListener.createEntityManager();

        // Check if we have an existing appointment today in db
        TypedQuery<Appointment> qApp = em.createQuery("FROM Appointment a WHERE a.account.accountId=:accountId AND a.departureDate IS NULL AND a.arrivalDate BETWEEN :startDate AND :endDate", Appointment.class);
        qApp.setParameter("accountId", accountId);
        DateTime dt = new DateTime();
        qApp.setParameter("startDate", dt.toDateMidnight().toDate());
        qApp.setParameter("endDate", dt.plusDays(1).toDateMidnight().toDate());

        try {
            Appointment appointment = qApp.getSingleResult(); //TODO manage several appointments per day
            vo = populateAppointmentVO(appointment);
            isAppExist = true;
            logger.debug("Retrieve existing Appointment. " + appointment);
        } catch (NoResultException nre) {
            logger.info("No current appointment open; we need to create a new one.");
        }

        // Else Create a new appointment from blank
        if (!isAppExist) {
            User u = null;
            List<Child> children = null;
            try {
                //get Possible Children
                TypedQuery<Child> qChild = em.createQuery("FROM Child c WHERE c.account.accountId=:accountId", Child.class);
                qChild.setParameter("accountId", accountId);
                children = qChild.getResultList();
                //get User
                u = em.find(User.class, userId);

            } catch (NoResultException nre) {
                logger.error(String.format("ERROR: No result found with parameters accountId:%d, userId:%d\n", accountId, userId));
                return null;
            } catch (Exception e) {
                logger.error(String.format("ERROR: Exception with parameters accountId:%d, userId:%d\n", accountId, userId), e);
                return null;
            } finally {
                em.close();
            }

            vo = new AppointmentVO();
            vo.setArrivalUser(UserVO.populate(u));
            vo.setDepartureUser(UserVO.populate(u));
            vo.setAccountId(accountId);
        }
        //TODO populate AppointmentVo wit app

        //else create a new appointment from scratch
        Date currentDate = new Date();

        String dateStr = Constants.sdf.format(currentDate);
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) {
            //we consider that is the arrival
            vo.setArrivalDate(dateStr);
            // vo.setDeclarationType("arrival");
        } else {
            //we consider that is the departure
            vo.setDepartureDate(dateStr);
            //vo.setDeclarationType("departure");
        }
        return vo;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AppointmentVO registerAppointment(AppointmentVO appointment) {
        logger.debug("register appointment " + appointment);
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        Appointment entity = null;
        try {

            if (appointment.getAppointmentId() != null) {
                //TODO check
                TypedQuery<Appointment> qApp = em.createQuery("FROM Appointment a WHERE a.account.accountId=:accountId AND a.arrivalDate EQUALS CURRENT_DATE", Appointment.class);
                qApp.setParameter("accountId", appointment.getAccountId());
                Appointment app = qApp.getSingleResult();

            } else {
                entity = new Appointment();
            }

            Query qChild = em.createQuery("FROM Child c WHERE childId IN :childIds");
            qChild.setParameter("childIds", appointment.getKidIds());
            List<Child> children = qChild.getResultList();

            Date d = null;
            User u = null;
            if (appointment.getArrivalDate() != null && !"".equals(appointment.getArrivalDate())) {
                d = Constants.sdf.parse(appointment.getArrivalDate());
                 u = em.find(User.class, appointment.getArrivalUser().getUserId());
                entity.setArrivalDate(d);
                entity.setArrivalUser(u);
            }

            entity.setArrivalDate(d);
            if (appointment.getDepartureDate() != null && !"".equals(appointment.getDepartureDate())) {
                d = Constants.sdf.parse(appointment.getDepartureDate());
                 u = em.find(User.class, appointment.getDepartureUser().getUserId());
                entity.setDepartureDate(d);
                entity.setDepartureUser(u);
            }

            entity.setAccount(u.getAccount());
            entity.setChildren(children);
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            appointment.setAppointmentId(entity.getAppointmentId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return appointment;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{appointmentId}")
    public AppointmentVO saveAppointment(@PathParam("appointmentId") Integer appointmentId, AppointmentVO appointment) {
        logger.debug("Update appointment " + appointment);
        EntityManager em = EntityManagerLoaderListener.createEntityManager();

        try {
            Appointment app = em.find(Appointment.class, appointmentId);
            User da, du;
            Query qChild = em.createQuery("FROM Child c WHERE childId IN :childIds");
            qChild.setParameter("childIds", appointment.getKidIds());
            List<Child> children = qChild.getResultList();
            Account account = null;


            if (appointment.getArrivalUser() == null) {
                 du = em.find(User.class,  appointment.getDepartureUser().getUserId());
                 account = app.getAccount();
                app.setDepartureUser(du);

            }
            if(appointment.getDepartureUser() == null) {
                da = em.find(User.class,  appointment.getArrivalUser().getUserId());
                app.setArrivalUser(da);
            }

            Date d = Constants.sdf.parse(appointment.getArrivalDate());
            app.setArrivalDate(d);
            d = Constants.sdf.parse(appointment.getDepartureDate());
            app.setDepartureDate(d);

            app.setAccount(account);
            app.setChildren(children);
            em.getTransaction().begin();
            em.merge(app);
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return appointment;
    }

    @GET
    @Path("/delete/{appointmentId}")
    public Response deleteById(@PathParam("appointmentId") Integer appointmentId) {
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            em.getTransaction().begin();
            Appointment app = em.find(Appointment.class, appointmentId);
            em.remove(app);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("ERROR in deleteById", e);
        } finally {
            em.close();
        }
        return Response.ok().build();
    }
}
