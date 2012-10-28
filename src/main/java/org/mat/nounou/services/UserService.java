package org.mat.nounou.services;

import org.mat.nounou.model.User;

import javax.persistence.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/user")
@Produces("application/json;charset=UTF-8")
public class UserService {

    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");

    }

    @GET
    public List<User> get() {
        System.out.println("Get Users service");
        EntityManager em=  entityManagerFactory.createEntityManager();
        TypedQuery<User> query = em.createQuery("FROM User", User.class);
        query.setMaxResults(200);
        List<User> users = query.getResultList();
        return users;
    }


    @GET
    @Path("/create/{firstName}/${lastName}")
    public User create(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        System.out.printf("create service for user %s %s \n", firstName, lastName);
        User user = new User(firstName, lastName);
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


    @GET
    @Path("/{name}")
    public User findByName(@PathParam("name") String name) {
        User u = null;
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            TypedQuery<User> query = entityManager.createQuery("FROM User WHERE name=:name", User.class);
            query.setMaxResults(200);
            query.setParameter("name", name);
            u = query.getSingleResult();
            entityManager.close();

        } catch(NoResultException nre){
            System.out.println("No result found for " + name);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }


}
