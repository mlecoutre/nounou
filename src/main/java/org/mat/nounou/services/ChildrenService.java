package org.mat.nounou.services;

import org.mat.nounou.model.Account;
import org.mat.nounou.model.Child;
import org.mat.nounou.model.Nurse;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.util.Constants;
import org.mat.nounou.vo.ChildVO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;
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
    public List<ChildVO> get() {
        System.out.println("Get Child service");
        List<ChildVO> children = new ArrayList<ChildVO>();
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        TypedQuery<Child> query = em.createQuery("FROM Child", Child.class);
        query.setMaxResults(200);
        List<Child> kids = query.getResultList();
        for (Child c : kids) {
            ChildVO vo = new ChildVO();
            vo.setAccountId(c.getAccount().getAccountId());
            vo.setBirthday(Constants.sdf.format(c.getBirthday()));
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

        try {
            childEntity.setBirthday(Constants.sdfDate.parse(child.getBirthday()));
        } catch (ParseException e) {
            e.printStackTrace();  //TODO manage error
        }
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();


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
            em.close();
            child.setChildId(childEntity.getChildId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return child;
    }


    @GET
    @Path("/account/{accountId}")
    public List<ChildVO> findByAccountId(@PathParam("accountId") Integer accountId) {
        List<ChildVO> cList = new ArrayList<ChildVO>();
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();

            TypedQuery<Child> query = em.createQuery("FROM Child c WHERE c.account.accountId=:accountId", Child.class);
            query.setMaxResults(Constants.MAX_RESULT);
            query.setParameter("accountId", accountId);
            List<Child> children = query.getResultList();

            //populate vo
            for (Child c : children) {
                ChildVO vo = new ChildVO();
                vo.setAccountId(c.getAccount().getAccountId());
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
            em.close();

        } catch (NoResultException nre) {
            System.out.println("No result found for accountId:= " + accountId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cList;
    }


    @GET
    @Path("/delete/{childId}")
    public Response deleteById(@PathParam("childId") Integer childId) {
        List<Child> c = null;
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Child WHERE childId=:childId");

            query.setParameter("childId", childId);
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }


}
