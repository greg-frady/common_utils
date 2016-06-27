package com.binaryapothecary.common_utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class to load in properties from files on the classpath of the project.
 *
 * Created by grefrady on 2/11/15.
 */
public class PropertyUtils {

    protected static final Logger logger = Logger.getLogger(PropertyUtils.class);

    private static Map<String, Properties> propsCache = new HashMap<String, Properties>();

    /**
     * Loads the properties for a given file name.
     *
     * @param propertiesFileName the name of the properties file to load
     * @return a Properties instance representing the file loaded
     */
    public static Properties loadProperties(String propertiesFileName) {

        Properties props;

        // Check the cache
        if (propsCache.containsKey(propertiesFileName)) {
            props = propsCache.get(propertiesFileName);
        }

        // Get the props from file
        else {

            props = new Properties();

            InputStream inputStream = null;

            try {
                inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream(propertiesFileName);
                props.load(inputStream);

                // Put the props in cache
                propsCache.put(propertiesFileName, props);
            } catch (IOException ex) {
                logger.error(String.format("Exception caught while loading properties: %s", propertiesFileName), ex);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error("Exception caught while closing inputStream: ", e);
                    }
                }
            }
        }

        return props;
    }
}
