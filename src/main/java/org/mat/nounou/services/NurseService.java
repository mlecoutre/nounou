package org.mat.nounou.services;

import org.mat.nounou.model.Nurse;
import org.mat.nounou.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/nurses")
public class NurseService {

    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Nurse registerNurse(Nurse nurse) {
        System.out.println("register " + nurse);
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(nurse);

            entityManager.getTransaction().commit();

            entityManager.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nurse;
    }

/*
    @GET
    @Path("/{firstName}")
    public User findByName(@PathParam("firstName") String firstName) {
        User u = null;
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            TypedQuery<User> query = entityManager.createQuery("FROM User WHERE firstName=:firstName", User.class);
            query.setMaxResults(200);
            query.setParameter("firstName", firstName);
            u = query.getSingleResult();
            entityManager.close();

        } catch (NoResultException nre) {
            System.out.println("No result found for " + firstName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }*/


}
