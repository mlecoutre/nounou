package org.mat.nounou.services;

import org.example.model.Appointment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 28/10/12
 * Time: 11:13
 */
@Path("/user")
@Produces("application/json;charset=UTF-8")
public class AppointmentService {


    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");

    }

    @GET
    public List<Appointment> get() {
        System.out.println("Appointment service");
        EntityManager em=  entityManagerFactory.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("FROM Appointment", Appointment.class);
        query.setMaxResults(200);
        List<Appointment> appointments = query.getResultList();
        return appointments;
    }
}
