package org.mat.nounou.servlets;

import org.mat.nounou.util.HerokuURLAnalyser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Initialize EntityManager Factory
 * For Heroku, try to find the environment property DATABASE_URL, and transform
 * it into a valid jdbc URL to initialize properly the DB.
 *
 * @author mlecoutre
 */
public class EntityManagerLoaderListener implements ServletContextListener {

    private static EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("WebListener start entity manager");
        //ClassNotFoundException: org/postgresql/ssl/NonValidatingFactory;
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            System.out.println("No DATABASE_URL set. Use default config in persistence.xml");
            emf = Persistence.createEntityManagerFactory("default");
        } else {
            HerokuURLAnalyser analyser = new HerokuURLAnalyser(databaseUrl);
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.url", analyser.generateJDBCUrl());
            properties.put("javax.persistence.jdbc.user", analyser.getUserName());
            properties.put("javax.persistence.jdbc.password", analyser.getPassword());

            if (analyser.getDbVendor().equals("postgres")) {
                System.out.println("set driver for postgres");
                properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            }
            emf = Persistence.createEntityManagerFactory("default", properties);
        }
    }

    /**
     * Close the entity manager
     *
     * @param event ServletContextEvent not used
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        emf.close();
    }

    /**
     * Create the EntityManager
     *
     * @return EntityManager
     */
    public static EntityManager createEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }

        return emf.createEntityManager();
    }


}