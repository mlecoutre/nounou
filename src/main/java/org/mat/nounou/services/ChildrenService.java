package org.mat.nounou.services;

import org.mat.nounou.model.Child;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.servlets.EntityManagerLoaderListener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/children")
public class ChildrenService {



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Child registerNurse(Child child) {
        System.out.println("register " + child);
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            em.getTransaction().begin();
            em.persist(child);

            em.getTransaction().commit();

            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return child;
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
