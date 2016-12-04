package hu.neuron.imaginer.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

public class JcrUtil {
	public static final String JCR_PROPERTIES = "app-config/imaginer_jcr.properties";
	public static final String JCR_IMPL_CLASS = "jcr_impl_class";
	public static final String JCR_REPO_USERNAME = "jcr_username";
	public static final String JCR_REPO_PASSWORD = "jcr_password";
	public static final String SLASH = "/";
	
	private static JcrInterface jcrImpl;
	private static Properties prop;

	static {
		Session session = null;
		try {
			prop = new Properties();
			File jcrPropertiesFile = new File(JCR_PROPERTIES);
			if (jcrPropertiesFile.exists()) {
				prop.load(new FileInputStream(jcrPropertiesFile));
			} else {
				prop.load(JcrUtil.class.getClassLoader().getResourceAsStream(JCR_PROPERTIES));
			}
			String jcrImplClassName = prop.getProperty(JCR_IMPL_CLASS);
			jcrImpl = (JcrInterface) Class.forName(jcrImplClassName).newInstance();

			session = createSession();

			session.save();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				session.logout();
			} catch (Exception e) {
			}
		}
	}

	public static Node getNode(String xpath, Session session) throws RepositoryException {
		QueryManager queryManager = session.getWorkspace().getQueryManager();
		Query query = queryManager.createQuery(xpath, "xpath");
		QueryResult result = query.execute();
		if ((result.getNodes() != null) && (result.getNodes().hasNext())) {
			return result.getNodes().nextNode();
		}
		return null;
	}

	public static NodeIterator getNodes(String xpath, Session session) throws RepositoryException {
		QueryManager queryManager = session.getWorkspace().getQueryManager();
		Query query = queryManager.createQuery(xpath, "xpath");
		QueryResult result = query.execute();
		return result.getNodes();
	}

	public static Node getNodeByAbsolutePath(String absolutePath, Session session)
			throws PathNotFoundException, RepositoryException {
		return session.getNode(absolutePath);
	}

	public static Session createSession(Repository repository) throws LoginException, RepositoryException {
		return repository.login(new SimpleCredentials(prop.getProperty(JCR_REPO_USERNAME),
				prop.getProperty(JCR_REPO_PASSWORD).toCharArray()));
	}

	public static Session createSession() throws LoginException, RepositoryException {
		return getRepository().login(new SimpleCredentials(prop.getProperty(JCR_REPO_USERNAME),
				prop.getProperty(JCR_REPO_PASSWORD).toCharArray()));
	}
	
	public static void closeSession(final Session session) {
		if (session != null) {
			session.logout();
		}
	}

	public static Repository getRepository() {
		return jcrImpl.getRepository();
	}
}
