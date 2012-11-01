package org.example.models;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * AppNounouTest
 * 
 * @author E010925
 * 
 */
//@RunWith(Arquillian.class)
public class AppNounouTest {

	private static final String DBUNIT_FILE = "/dataset.xml";

	private static IDatabaseConnection dbUnitConn;

    private static EntityManagerFactory emf;
//	@Inject
//	private EntityManager em;
//
//	@Inject
//	private Logger log;
//
//	@Inject
//	private PersonService personService;

	/**
	 * create archive
	 * 
	 * @return arquilian archive
	 */
	//@Deployment
	public static JavaArchive createTestArchive() {
		return ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addPackages(true, "org.mat.nounou")
				//.addClasses(AuditDataProducer.class)
				.addAsManifestResource(EmptyAsset.INSTANCE,
						ArchivePaths.create("beans.xml"))
				.addAsManifestResource("META-INF/persistence.xml",
						ArchivePaths.create("persistence.xml"));

	}

	/**
	 * initTable
	 * 
	 * @throws Exception
	 *             on error
	 */
	@Before
	public void initTable() throws Exception {
        emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
		try {

			Session session = ((Session) em.getDelegate());
			session.doWork(new Work() {

				@Override
				public void execute(Connection connection) throws SQLException {

					try {

						dbUnitConn = new DatabaseConnection(connection);

						FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
						builder.setColumnSensing(true);
						IDataSet dataSet = builder
								.build(AppNounouTest.class
										.getResourceAsStream(DBUNIT_FILE));

						DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn,
								dataSet);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});

			// dbUnitConn.close();
		} catch (Throwable th) {
			th.printStackTrace();
		}   finally {
            em.close();
        }
	}

	/**
	 * testAddPerson
	 * 
	 * @throws Exception
	 *             on error
	 */
	@Test
	public void testAddPerson() throws Exception {
		//log.debug("testAddPerson");
/*
		Person person = new Person();
		person.setFirstname("firstname");
		person.setLastname("lastname");
		person.setBirthday(new Date());
		person.setSex('M');
		boolean result = personService.create(person);
		log.debug("Person inserted");

		assertTrue("Person should be created", result);*/
	}

	/**
	 * testFindall
	 */
	@Test
	public void testFindall() throws Exception {
		//log.debug("testFindall");

	/*	List<Person> persons = personService.findByLastname("%");
		for (Person p : persons) {
			log.debug("> " + p);
		}
		assertTrue(
				"We should have 5 peoples in the DB and we have "
						+ persons.size(), persons.size() == 5);*/
	}

}
