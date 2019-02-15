/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Data Base Session Management Class.
 */
public class SessionManager {

  /** Data Base Session Management Instance. */
  private static SessionManager instance = new SessionManager();
  /** Session. */
  private SessionFactory sessions;

  /**
   * Private Constructor.
   *
   * @throws HibernateException
   *           exception at Hibernate
   */
  private SessionManager() {
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() 
        .build();

    MetadataSources ms = new MetadataSources(registry);
    Metadata m = ms.buildMetadata();
    sessions = m.buildSessionFactory();
  }

  /**
   * Instance Acquisition.
   *
   * @return data base session management class instance
   */
  public static synchronized SessionManager getInstance() {
    return instance;
  }

  /**
   * Session Acquisition.
   *
   * @return session class instance
   * @throws HibernateException
   *           flamework exception
   */
  public Session getSession() throws HibernateException {
    return sessions.openSession();
  }
}
