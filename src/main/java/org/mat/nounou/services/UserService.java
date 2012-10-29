package org.mat.nounou.services;

import org.mat.nounou.model.User;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/users")
public class UserService {

    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> get() {
        System.out.println("Get Users service");
        List<User> users = null;
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = em.createQuery("FROM User", User.class);
        query.setMaxResults(200);
        users = query.getResultList();
        return users;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User registerUser(User user) {
        System.out.println("register " + user);
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(user);

            entityManager.getTransaction().commit();

            entityManager.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
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
