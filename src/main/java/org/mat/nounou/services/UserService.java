package org.mat.nounou.services;

import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> get() {
        System.out.println("Get Users service");
        List<User> users = null;
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<User> query = em.createQuery("FROM User", User.class);
        query.setMaxResults(200);
        users = query.getResultList();
        return users;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        System.out.println("register " + user);
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            em.getTransaction().begin();
            em.persist(user);

            em.getTransaction().commit();

            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.created(URI.create("/users/"+user.getUserId())).entity(user).build();
    }


    @GET
    @Path("/{userId}")
    public User findByName(@PathParam("userId") String userId) {
        User u = null;
        try {

            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            TypedQuery<User> query = em.createQuery("FROM User WHERE userId=:userId", User.class);
            query.setMaxResults(200);
            query.setParameter("userId", userId);
            u = query.getSingleResult();
            em.close();

        } catch (NoResultException nre) {
            System.out.println("No result found for userId=" + userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }


}
