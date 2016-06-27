package com.binaryapothecary.common_utils.web;

import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Contains methods useful for making web service calls.
 *
 * Created by grefrady on 2/22/16.
 */
public class WebRequestUtils {

    private static Logger log = Logger.getLogger(WebRequestUtils.class);

    public enum MediaType {
        APP_JSON("application/json"),
        APP_JAVASCRIPT("application/javascript"),
        APP_XML("application/xml"),
        URL_ENCODED("application/x-www-form-urlencoded");

        private String value;

        MediaType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     * Performs a POST call with a JSON payload.
     *
     * @param url the http endpoint
     * @param body the request body (in JSON formatted String)
     * @param acceptType the 'Accept' header value
     * @param contentType the 'Content-Type' value
     * @return a ResponseContainer with response results and status
     */
    public static ResponseContainer post(String url, String body, MediaType acceptType, MediaType contentType) {
        ResponseContainer container = new ResponseContainer();

        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", acceptType.value);

            if (!acceptType.equals(MediaType.URL_ENCODED)) {
                con.setRequestProperty("Content-Type", contentType.value);
            }

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(body);
            wr.flush();
            wr.close();

            HttpStatusCode statusCode = HttpStatusCode.getStatusCode(con.getResponseCode());
            container.httpStatusCode = statusCode;

            if (HttpStatusCode.OK.equals(statusCode)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                container.body = response.toString();
            }
            else {
                container.body = con.getResponseCode() + " " + con.getResponseMessage();
            }
        } catch (Exception e) {
            log.error("Exception caught while making POST call: ", e);
        }

        return container;
    }

    /**
     * Performs a GET call using given parameters.
     *
     * @param url the http endpoint
     * @param oauthToken the oauth token to be used in call
     * @param acceptType the 'Accept' header value
     * @return a ResponseContainer with response results and status
     */
    public static ResponseContainer get(String url, String oauthToken, MediaType acceptType) {
        ResponseContainer container = new ResponseContainer();

        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + oauthToken);
            con.setRequestProperty("Accept", acceptType.value);

            HttpStatusCode statusCode = HttpStatusCode.getStatusCode(con.getResponseCode());
            container.httpStatusCode = statusCode;

            if (HttpStatusCode.OK.equals(statusCode)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                container.body = response.toString();
            } else {
                container.body = con.getResponseCode() + " " + con.getResponseMessage();
            }

        } catch (Exception e) {
            log.error("Exception caught while making GET call: ", e);
        }

        return container;
    }

    /**
     * Response results container
     */
    public static class ResponseContainer {
        public String body;
        public HttpStatusCode httpStatusCode;
    }
}

