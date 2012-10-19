package loclock.server;


import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * Manages the persistence of objects
 */
public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private PMF() {}
	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}
}
