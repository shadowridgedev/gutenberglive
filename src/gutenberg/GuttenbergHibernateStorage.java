package gutenberg;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import gutenberg.Book;

public class GuttenbergHibernateStorage {
	private static SessionFactory factory;
	private static ServiceRegistry serviceRegistry;
	private static Session newSession;

	GuttenbergHibernateStorage() {

		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		config.addResource("Book.hbm.xml");
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		factory = config.buildSessionFactory(serviceRegistry);

	}

	void SaveBooks(List<Book> thebooks) {
		newSession = factory.openSession();
		newSession.beginTransaction();
		for (Book book : thebooks) {
			// System.out.println(book.getTitle() + " by " + book.getAuthor());
			saveBook(book);
		}
		newSession.getTransaction().commit();
		newSession.close();
	}

	void saveBook(Book book) {

		try {

			newSession.save(book);

		} catch (HibernateException e) {
			e.printStackTrace();
			newSession.getTransaction().rollback();
			newSession.close();
		}

	}

	void DeleteBooks(List<Book> savedBooks) {
		if (newSession != null)
			newSession.close();
		newSession = factory.openSession();
		newSession.beginTransaction();
		for (Book book : savedBooks) {
			System.out.println("Deleting " + book.getTitle() + " by " + book.getAuthor());
			DeleteBook(book);
		}
		newSession.getTransaction().commit();
		newSession.close();
	}

	public static void DeleteBook(Book book) {

		newSession.delete(book);
		;
	}

	public void close() {
		// TODO Auto-generated method stub

		if (newSession.isOpen())
			newSession.close();
		factory.close();
	}

	List<Book> returnBooks() {
		newSession = factory.openSession();
		newSession.beginTransaction();
		Query<Book> q = newSession.createNativeQuery("SELECT * FROM guttenberg.book").addEntity(gutenberg.Book.class);
		List<Book> result = q.getResultList();
		newSession.getTransaction().commit();
		newSession.close();
		return result;

	}

	List<Book> returnBook(String field, String value) {
		newSession = factory.openSession();
		newSession.beginTransaction();
		@SuppressWarnings("deprecation")
		Query<Book> q = newSession.createNativeQuery("SELECT * FROM guttenberg.book  where " + field + " = " + value)
				.addEntity(gutenberg.Book.class);
		newSession.getTransaction().commit();
		List<Book> result = q.getResultList();
		newSession.close();
		return result;

	}

	void emptyTable() {
		if (newSession != null)
			newSession.close();
		newSession = factory.openSession();
		String hql = String.format("delete from %s", "guttenberg.Book");
		newSession.beginTransaction();
		Query<Book> query = newSession.createQuery(hql);

		int res = query.executeUpdate();
		newSession.getTransaction().commit();
		newSession.close();
		return;
	}
}
