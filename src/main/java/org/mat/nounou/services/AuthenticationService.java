package org.mat.nounou.services;

import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;
import org.mat.nounou.vo.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Basic auth service
 * @author E010925
 * Date: 29/10/12
 * Time: 18:18
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(Token token) {
        logger.debug("Auth for " + token.getUid());
        User u = null;
        EntityManager em = EntityManagerLoaderListener.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("FROM User WHERE email=:email AND password=:password", User.class);
            query.setParameter("email", token.getUid());
            query.setParameter("password", token.getPassword());
            u = query.getSingleResult();
            token.setAccountId(u.getAccount().getAccountId());
            token.setUserId(u.getUserId());
            token.setUserName(u.getFirstName());
            token.setPassword("");//reset pwd to avoid resend it to the browser
        } catch (NoResultException nre) {
            logger.error("Invalid credential for " + token.getUid());
            return Response.status(500).build();
        } catch (Exception e) {
            logger.error("registerUser", e);
            return Response.status(500).build();
        } finally {
            em.close();
        }

        return Response.ok(token).build();

    }

}
