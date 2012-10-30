package org.mat.nounou.services;

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
@Path("/nurses")
@Produces(MediaType.APPLICATION_JSON)
public class NurseService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Nurse> get() {
        System.out.println("Get NurseVO service");
        List<Nurse> nurses = null;
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<Nurse> query = em.createQuery("FROM NurseVO", Nurse.class);
        query.setMaxResults(200);
        nurses = query.getResultList();
        return nurses;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Nurse registerNurse(Nurse nurse) {
        System.out.println("register " + nurse);
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            em.getTransaction().begin();
            em.persist(nurse);

            em.getTransaction().commit();

            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nurse;
    }


    @GET
    @Path("/user/{userId}")
    public List<Nurse> findByName(@PathParam("userId") Integer userId) {
        List<Nurse> nurses = null;
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();

            TypedQuery<Nurse> query = em.createQuery("Select n FROM NurseVO n,  UserVO u, ChildVO c WHERE n.nurseId= c.nurse.nurseId AND c.accountUser.userId=:userId", Nurse.class);
            query.setMaxResults(20);
            query.setParameter("userId", userId);
            nurses = query.getResultList();
            em.close();

        } catch (NoResultException nre) {
            System.out.println("No result found for userId:= " + userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nurses;
    }


}
