package org.mat.nounou.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

import org.mat.nounou.model.Account;
import org.mat.nounou.model.Child;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.util.Constants;
import org.mat.nounou.vo.ChildVO;

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
    public List<ChildVO> get() {
        System.out.println("Get Child service");
        List<Child> kids = null;
        List<ChildVO> children = new ArrayList<ChildVO>();
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            TypedQuery<Child> query = em.createQuery("FROM Child", Child.class);
            query.setMaxResults(Constants.MAX_RESULT);
            kids = query.getResultList();
        } catch (NoResultException nre) {
            System.out.println("No children found in db.");
        } finally {
            em.close();
        }
        for (Child c : kids) {
            ChildVO vo = new ChildVO();
            vo.setAccountId(c.getAccount().getAccountId());
            if (c.getBirthday() != null)
                vo.setBirthday(Constants.sdfDate.format(c.getBirthday()));
            vo.setChildId(c.getChildId());
            vo.setFirstName(c.getFirstName());
            vo.setLastName(c.getLastName());
            if (c.getNurse() != null) {
                vo.setNurseId(c.getNurse().getNurseId());
                vo.setNurseName(c.getNurse().getFirstName().concat(" ").concat(c.getNurse().getLastName()));
            }
            children.add(vo);
        }
        return children;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ChildVO registerKid(ChildVO child) {
        System.out.println("register Child " + child);
        Child childEntity = new Child();
        childEntity.setFirstName(child.getFirstName());
        childEntity.setLastName(child.getLastName());

        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            childEntity.setBirthday(Constants.sdfDate.parse(child.getBirthday()));
            TypedQuery<Account> qAccount = em.createQuery("FROM Account a WHERE accountId=:accountId", Account.class);
            qAccount.setParameter("accountId", child.getAccountId());
            Account account = qAccount.getSingleResult();
            childEntity.setAccount(account);

            TypedQuery<Nurse> qNurse = em.createQuery("FROM Nurse n WHERE nurseId=:nurseId", Nurse.class);
            qNurse.setParameter("nurseId", child.getNurseId());
            Nurse nurse = qNurse.getSingleResult();
            childEntity.setNurse(nurse);
            em.getTransaction().begin();
            em.persist(childEntity);
            em.getTransaction().commit();
            child.setChildId(childEntity.getChildId());
            child.setNurseName(nurse.getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return child;
    }

    @GET
    @Path("/account/{accountId}")
    public List<ChildVO> findByAccountId(@PathParam("accountId") Integer accountId) {
        List<ChildVO> cList = new ArrayList<ChildVO>();
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            TypedQuery<Child> query = em.createQuery("FROM Child c WHERE c.account.accountId=:accountId", Child.class);
            query.setMaxResults(Constants.MAX_RESULT);
            query.setParameter("accountId", accountId);
            List<Child> children = query.getResultList();

            //populate vo
            for (Child c : children) {
                ChildVO vo = new ChildVO();
                vo.setAccountId(c.getAccount().getAccountId());
                if (c.getBirthday() != null) //birthday is an optional parameter
                    vo.setBirthday(Constants.sdfDate.format(c.getBirthday()));
                vo.setChildId(c.getChildId());
                vo.setFirstName(c.getFirstName());
                vo.setLastName(c.getLastName());
                if (c.getNurse() != null) {
                    vo.setNurseId(c.getNurse().getNurseId());
                    vo.setNurseName(c.getNurse().getFirstName().concat(" ").concat(c.getNurse().getLastName()));
                }
                cList.add(vo);
            }
        } catch (NoResultException nre) {
            System.out.println("No result found for accountId:= " + accountId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return cList;
    }


    @GET
    @Path("/delete/{childId}")
    public Response deleteById(@PathParam("childId") Integer childId) {
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Child WHERE childId=:childId");

            query.setParameter("childId", childId);
            query.executeUpdate();
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return Response.ok().build();
    }


}
