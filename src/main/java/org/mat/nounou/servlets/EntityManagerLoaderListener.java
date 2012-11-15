package org.mat.nounou.servlets;

import org.mat.nounou.util.HerokuURLAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(EntityManagerLoaderListener.class);

    private String DEFAULT_DB_URL = "jdbc:h2:~/test.db";
    private static EntityManagerFactory emf;
    private boolean pushAdditionalProperties = true;

    public EntityManagerLoaderListener() {
    }

    public EntityManagerLoaderListener(boolean pushAdditionalProperties) {
        this.pushAdditionalProperties = pushAdditionalProperties;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.debug("WebListener start entity manager");
        String databaseUrl = System.getenv("DATABASE_URL");

        if (databaseUrl == null && pushAdditionalProperties) {
            logger.debug("Use default config in persistence.xml with " + DEFAULT_DB_URL);
            databaseUrl = DEFAULT_DB_URL;
        }
        Map<String, String> properties = new HashMap<String, String>();
        if (pushAdditionalProperties) {
            HerokuURLAnalyser analyser = new HerokuURLAnalyser(databaseUrl);

            logger.debug("SET JDBC URL TO " + analyser.getJdbcURL());
            properties.put("javax.persistence.jdbc.url", analyser.getJdbcURL());
            properties.put("javax.persistence.jdbc.user", analyser.getUserName());
            properties.put("javax.persistence.jdbc.password", analyser.getPassword());

            if ("postgres".equals(analyser.getDbVendor())) {
                logger.debug("SET DRIVER FOR postgres");
                properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
                properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            } else if ("h2".equals(analyser.getDbVendor())) {
                logger.debug("SET DRIVER FOR h2");
                properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
            }
        }
        emf = Persistence.createEntityManagerFactory("default", properties);


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


    public boolean isPushAdditionalProperties() {
        return pushAdditionalProperties;
    }

    public void setPushAdditionalProperties(boolean pushAdditionalProperties) {
        this.pushAdditionalProperties = pushAdditionalProperties;
    }
}