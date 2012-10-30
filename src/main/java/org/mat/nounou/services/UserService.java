package org.mat.nounou.services;

import org.apache.commons.beanutils.BeanUtils;
import org.mat.nounou.model.Account;
import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.UserVO;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * UserVO: mlecoutre
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
        TypedQuery<User> query = em.createQuery("FROM UserVO", User.class);
        query.setMaxResults(200);
        users = query.getResultList();
        return users;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserVO user) {
        System.out.println("register " + user);
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();

            User u = new User();
            Account account = null;
            BeanUtils.populate(u, BeanUtils.describe(user));
            if  (user.getAccountId() == null){
             account = new Account();
            }else{
                TypedQuery<Account> tQ=  em.createQuery("FROM Account where accountId=:accountId", Account.class);
                tQ.setParameter("accountId", user.getAccountId());
                account = tQ.getSingleResult();
            }
            u.setAccount(account);
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            user.setAccountId(u.getAccount().getAccountId());
            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.created(URI.create("/users/"+user.getUserId())).entity(user).build();
    }


    @GET
    @Path("/{userId}")
    public UserVO findByName(@PathParam("userId") String userId) {
        UserVO u = null;
        User user = null;
        try {

            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            TypedQuery<User> query = em.createQuery("FROM UserVO WHERE userId=:userId", User.class);
            query.setMaxResults(200);
            query.setParameter("userId", new Integer(userId));
            user = query.getSingleResult();
            em.close();
            BeanUtils.populate(u, BeanUtils.describe(user));
            u.setAccountId(user.getAccount().getAccountId());
        } catch (NoResultException nre) {
            System.out.println("No result found for userId=" + userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }


}
