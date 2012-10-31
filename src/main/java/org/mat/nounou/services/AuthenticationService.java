package org.mat.nounou.services;

import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.Token;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * UserVO: E010925
 * Date: 29/10/12
 * Time: 18:18
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationService {

    private static Integer accountId = 1;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(Token token) {
        System.out.println("Auth for " + token.getUid());
        User u;
        try {
            EntityManager em = EntityManagerLoaderListener.createEntityManager();
            TypedQuery<User> query = em.createQuery("FROM User WHERE email=:email AND password=:password", User.class);
            query.setParameter("email", token.getUid());
            query.setParameter("password", token.getPassword());
            u = query.getSingleResult();
            token.setAccountId(u.getAccount().getAccountId());
            token.setUserId(u.getUserId());
            token.setUserName(u.getFirstName());
            token.setPassword("");//reset pwd

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
        NewCookie cookie = new NewCookie("token", "{\"accountId\": \"" + token.getAccountId() +
                "\", \"userId\":\"" + token.getUserId() + "\"}" );

        return Response.ok(token).cookie(cookie).build();

    }

    public static Integer getAccountId() {
        return accountId;
    }


}
