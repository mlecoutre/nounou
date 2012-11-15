package org.mat.nounou.services;

import org.apache.commons.beanutils.BeanUtils;
import org.mat.nounou.model.Account;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.util.Constants;
import org.mat.nounou.vo.NurseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Value Object for Nurse
 * NurseVO: mlecoutre
 * Date: 27/10/12
 * Time: 12:01
 */
@Path("/nurses")
@Produces(MediaType.APPLICATION_JSON)
public class NurseService {

    private static final Logger logger = LoggerFactory.getLogger(NurseService.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NurseVO> get() {
        logger.debug("Get NurseVO service");
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
            logger.warn("No nurse found in the DB");
        } catch (Exception e) {
            logger.error("ERROR get", e);
        } finally {
            em.close();
        }
        return nurses;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{nurseId}")
    public NurseVO getByNurseId(@PathParam("nurseId") Integer nurseId) {
        logger.debug("Get Nurse by Id service");
        NurseVO nvo = new NurseVO();
        List<NurseVO> nurses = new ArrayList<NurseVO>();
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            Nurse nurse = em.find(Nurse.class, nurseId);
            BeanUtils.populate(nvo, BeanUtils.describe(nurse));
            nurses.add(nvo);
        } catch (NoResultException nre) {
            logger.warn("No nurse found in the DB");
        } catch (Exception e) {
            logger.error("ERROR get", e);
        } finally {
            em.close();
        }
        return nvo;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NurseVO registerNurse(NurseVO nurse) {
        logger.debug("Register " + nurse);
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        Nurse entity = new Nurse();
        try {
            Account account = em.find(Account.class, nurse.getAccountId());
            BeanUtils.populate(entity, BeanUtils.describe(nurse));
            entity.setNurseId(null);
            em.getTransaction().begin();
            account.getNurses().add(entity);
            em.persist(entity);
            em.getTransaction().commit();
            nurse.setNurseId(entity.getNurseId());
        } catch (Exception e) {
            logger.error("ERROR registerNurse", e);
        } finally {
            em.close();
        }
        return nurse;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{nurseId}")
    public NurseVO UpdateNurse(NurseVO nurse,@PathParam("nurseId") Integer nurseId) {
        logger.debug("Update " + nurse);
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        Nurse entity = new Nurse();
        try {
            em.getTransaction().begin();
            Nurse n = em.find(Nurse.class, nurse.getNurseId());
            n.setFirstName(nurse.getFirstName());
            n.setLastName(nurse.getLastName());
            n.setPhoneNumber(nurse.getPhoneNumber());
            em.getTransaction().commit();
            nurse.setNurseId(entity.getNurseId());
        } catch (Exception e) {
            logger.error("ERROR UpdateNurse", e);
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
            logger.warn("No nurse result found for accountId:= " + accountId);
        } catch (Exception e) {
            logger.error("ERROR findByAccountId", e);
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
            Nurse n = em.find(Nurse.class, nurseId);
            Account account = em.find(Account.class, accountId);
            account.getNurses().remove(n);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("ERROR deleteForAnAccount", e);
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
            Nurse n = em.find(Nurse.class, nurseId);
            TypedQuery<Set> accQuery = em.createQuery("SELECT n.employers FROM  Nurse n WHERE n.nurseId=:nurseId", Set.class);
            accQuery.setParameter("nurseId", nurseId);
            List accounts = accQuery.getResultList();
            for (Object a : accounts) {
                Account account = (Account) a;
                account.getNurses().remove(n);
            }
            em.remove(n);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("ERROR deleteById", e);
            return Response.serverError().build();
        } finally {
            em.close();
        }
        return Response.ok().build();
    }

}
