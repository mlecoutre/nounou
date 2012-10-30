package org.mat.nounou.services;

import org.mat.nounou.model.User;
import org.mat.nounou.servlets.EntityManagerLoaderListener;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * User: E010925
 * Date: 29/10/12
 * Time: 18:18
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        System.out.println("auth " + user);
        User u ;
        try {
           EntityManager em = EntityManagerLoaderListener.createEntityManager();
            TypedQuery<User> query = em.createQuery("FROM User WHERE email=:email", User.class);
            query.setMaxResults(1);
            query.setParameter("email", user.getEmail());
            u = query.getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.created(URI.create("/users/" + user.getUserId())).entity(user).build();
    }
}
