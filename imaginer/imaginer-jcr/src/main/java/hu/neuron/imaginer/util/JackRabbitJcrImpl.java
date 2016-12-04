package hu.neuron.imaginer.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jcr.Repository;

import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JackRabbitJcrImpl implements JcrInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(JackRabbitJcrImpl.class.getName());
	public static final String JACKRABBIT_JNDI = "jackrabbit.jndi.name";
	public static final String JACKRABBIT_PROPERTIES = "app-config/jackrabbit.properties";
	private static Repository repo;

	static {
		try {
			Properties prop = new Properties();
			File jackrabbitProperties = new File(JACKRABBIT_PROPERTIES);
			if (jackrabbitProperties.exists()) {
				prop.load(new FileInputStream(jackrabbitProperties));
			} else {
				prop.load(JcrUtil.class.getClassLoader().getResourceAsStream(JACKRABBIT_PROPERTIES));
			}
			String jndiName = prop.getProperty(JACKRABBIT_JNDI);
			logger.info("Jackrabbit repository jndi: " + jndiName);
			Map<String, String> parameters = new HashMap<>();
			parameters.put("org.apache.jackrabbit.repository.jndi.name", jndiName);
			repo = JcrUtils.getRepository(parameters);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public Repository getRepository() {
		return repo;
	}
}
