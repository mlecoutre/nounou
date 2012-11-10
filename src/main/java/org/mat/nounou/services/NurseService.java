package org.mat.nounou.services;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.mat.nounou.model.Account;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.util.Constants;
import org.mat.nounou.vo.NurseVO;

/**
 * Value Object for Nurse
 * NurseVO: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/nurses")
@Produces(MediaType.APPLICATION_JSON)
public class NurseService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NurseVO> get() {
        System.out.println("Get NurseVO service");
        List<NurseVO> nurses = new ArrayList<NurseVO>();
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            TypedQuery<Nurse> query = em.createQuery("FROM Nurse", Nurse.class);
            query.setMaxResults(Constants.MAX_RESULT);
            List<Nurse> ns = query.getResultList();
            for (Nurse n : ns) {
                NurseVO nvo = new NurseVO();
                BeanUtils.populate(nvo, BeanUtils.describe(n));
                nurses.add(nvo);
            }
        } catch (NoResultException nre) {
            System.out.println("No nurse found in the DB");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return nurses;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NurseVO registerNurse(NurseVO nurse) {
        System.out.println("Register " + nurse);
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        Nurse entity = new Nurse();
        try {
            TypedQuery<Account> query = em.createQuery("FROM Account a WHERE a.accountId=:accountId", Account.class);
            query.setParameter("accountId", nurse.getAccountId());
            Account account = query.getSingleResult();

            BeanUtils.populate(entity, BeanUtils.describe(nurse));
            entity.setNurseId(null);
            em.getTransaction().begin();
            account.getNurses().add(entity);
            em.persist(entity);
            em.getTransaction().commit();
            nurse.setNurseId(entity.getNurseId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return nurse;
    }

    @GET
    @Path("/account/{accountId}")
    public List<NurseVO> findByAccountId(@PathParam("accountId") Integer accountId) {
        List<NurseVO> nurses = new ArrayList<NurseVO>();
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            TypedQuery<Collection> query = em.createQuery("SELECT a.nurses FROM Account a WHERE a.accountId=:accountId", Collection.class);
            query.setMaxResults(20);
            query.setParameter("accountId", accountId);
            List ns = query.getResultList();
            Iterator<Nurse> it = ns.iterator();
            while (it.hasNext()) {
                Nurse n = it.next();
                NurseVO nvo = new NurseVO();
                BeanUtils.populate(nvo, BeanUtils.describe(n));
                nurses.add(nvo);
            }

        } catch (NoResultException nre) {
            System.out.println("No nurse result found for accountId:= " + accountId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return nurses;
    }

    @GET
    @Path("/delete/{nurseId}/accountId/{accountId}")
    public Response deleteForAnAccount(@PathParam("nurseId") Integer nurseId, @PathParam("accountId") Integer accountId) {
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Nurse> query = em.createQuery("FROM Nurse WHERE nurseId=:nurseId", Nurse.class);
            query.setParameter("nurseId", nurseId);
            Nurse n = query.getSingleResult();

            TypedQuery<Account> accQuery = em.createQuery("FROM  Account a WHERE a.accountId=:accountId", Account.class);
            accQuery.setParameter("accountId", accountId);
            Account account = accQuery.getSingleResult();
            account.getNurses().remove(n);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        } finally {
            em.close();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/delete/{nurseId}")
    public Response deleteById(@PathParam("nurseId") Integer nurseId) {
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {

            em.getTransaction().begin();
            TypedQuery<Nurse> query = em.createQuery("FROM Nurse WHERE nurseId=:nurseId", Nurse.class);
            query.setParameter("nurseId", nurseId);
            Nurse n = query.getSingleResult();

            TypedQuery<Set> accQuery = em.createQuery("SELECT n.employers FROM  Nurse n WHERE n.nurseId=:nurseId", Set.class);
            accQuery.setParameter("nurseId", nurseId);
            List accounts = accQuery.getResultList();
            for (Object a : accounts) {
                Account account =(Account) a;
                account.getNurses().remove(n);
            }
            em.remove(n);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        } finally {
            em.close();
        }
        return Response.ok().build();
    }

}
