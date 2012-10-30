package org.mat.nounou.services;

import org.mat.nounou.model.Child;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * UserVO: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/children")
@Produces(MediaType.APPLICATION_JSON)
public class ChildrenService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Child> get() {
        System.out.println("Get ChildVO service");
        List<Child> children = null;
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<Child> query = em.createQuery("FROM ChildVO", Child.class);
        query.setMaxResults(200);
        children = query.getResultList();
        return children;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Child registerNurse(Child child) {
        System.out.println("register ChildVO " + child);

        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            em.getTransaction().begin();

            //TODO update to attach children to other nurse and accountUser than 1
            TypedQuery<User> qUser = em.createQuery("FROM UserVO c WHERE userId=1", User.class);
            User u = qUser.getSingleResult();
            child.setAccountUser(u);

            TypedQuery<Nurse> qNurse = em.createQuery("FROM NurseVO n WHERE nurseId=1", Nurse.class);
            Nurse nurse = qNurse.getSingleResult();

            child.setNurse(nurse);
            em.persist(child);

            em.getTransaction().commit();

            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return child;
    }


    @GET
    @Path("/user/{userId}")
    public List<Child> findByUserId(@PathParam("userId") Integer userId) {
        List<Child> c = null;
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();

            TypedQuery<Child> query = em.createQuery("FROM ChildVO c WHERE c.accountUser.userId=:userId", Child.class);
            query.setMaxResults(200);
            query.setParameter("userId", userId);
            c = query.getResultList();
            em.close();

        } catch (NoResultException nre) {
            System.out.println("No result found for userId:= " + userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }


    @GET
    @Path("/delete/{childId}")
    public void deleteById(@PathParam("childId") Integer childId) {
        List<Child> c = null;
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM ChildVO WHERE childId=:childId");

            query.setParameter("childId", childId);
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
