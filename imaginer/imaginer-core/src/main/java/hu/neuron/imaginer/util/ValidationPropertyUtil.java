package hu.neuron.imaginer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.neuron.imaginer.validation.vo.ValidationFieldType;

public class ValidationPropertyUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ValidationPropertyUtil.class.getName());
	
	private static final String PROPERTIES_PATH = "app-config/validation.properties";
	private static final String REGEX_PREFIX = "imaginer.validator.regex";
	
	private static Properties properties;

	private static synchronized Properties getProperties() {
		if (properties != null) {
			return properties;
		}
		properties = new Properties();
		try {
			properties.load(new FileInputStream(PROPERTIES_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties;
	}

	public static synchronized Properties reloadProperties() {
		properties = null;
		return getProperties();
	}

	public static String getProperty(String key) {
		String property = getProperties().getProperty(key);
		if (property == null) {
			logger.info("Cannot find property with key: " + key);
		}
		return property;
	}
	
	public static String getValidatorRegex(ValidationFieldType type) {
		reloadProperties();
		String key = REGEX_PREFIX + '.' + type.toString().toLowerCase();
		String regex = getProperties().getProperty(key);
		if (regex == null) {
			logger.info("Cannot find validator regex for validation field type: " + key);
		}
		return regex;
	}
	
	public static void addProperty(String key, String value) {
		getProperties().put(key, value);
	}

}
