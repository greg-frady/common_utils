package com.binaryapothecary.common_utils;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by grefrady on 8/13/15.
 */
public class JsonUtils {

    private static Logger logger = Logger.getLogger(JsonUtils.class);
    private static Gson gson = new Gson();

    /**
     * Loads a json file and returns the representative class of the given type.
     *
     * @param jsonFileName the name of the json file to load
     * @param clazz the type of object represented by the json file
     * @return an object representing the given json file.
     */
    public static <T> T jsonFromFile(String jsonFileName, Class<T> clazz) throws Exception {

        InputStream inputStream = null;

        try {
            inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(jsonFileName);

            if (inputStream != null) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                return gson.fromJson(reader, clazz);
            }
        } catch (final Exception e) {
            logger.error("Exception caught during gson loading/parsing: ", e);
        }

        return null;
    }

    /**
     * Creates an object of the given clazz from the given jsonString.
     *
     * @param jsonString the json data
     * @param clazz the type of object represented by the json data
     * @return an object representing the given json data
     */
    public static <T> T jsonFromString(String jsonString, Class<T> clazz) throws Exception{

        if (!StringUtils.isEmpty(jsonString)) {
            return gson.fromJson(jsonString, clazz);
        }

        return null;
    }

}
