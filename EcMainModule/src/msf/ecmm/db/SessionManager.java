package msf.ecmm.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SessionManager {

	private SessionFactory sessions;

	private SessionManager() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.build();

		MetadataSources ms = new MetadataSources(registry);
		Metadata m = ms.buildMetadata();
		sessions = m.buildSessionFactory();
	}

	public static synchronized SessionManager getInstance() {
		return instance;
	}

	public Session getSession() throws HibernateException {
		return sessions.openSession();
	}
}
