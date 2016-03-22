package br.pucpr.ppgia.prototipo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory factory;
	private static Session session;
	
	public static Session getCurrentSession() {
		if (factory == null){
			factory = new Configuration().configure().buildSessionFactory();
			session = factory.openSession();
		}			
		if (!session.isOpen()){
			session = factory.openSession();
		}		
		return session;
	}

	public static void closeSession() {
		if (session.isOpen()){
			session.close();
		}
	}
	

}
