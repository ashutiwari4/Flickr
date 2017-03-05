package com.ashutosh.flicker.remote;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

/**
 * Created by Reetesh on 3/5/2017.
 */

public class WebUtils {


    private WebUtils() {
    }

    public static final String API_KEY = "f92d925d73f975062ddc0965aaeb3f68";
    public static final String baseUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&";
    public static final int RESULT_COUNT = 10;
    public static final String API_KEY_END_POINT = "?&api_key=" + API_KEY + "&per_page="+RESULT_COUNT+"&page=";
    public static final String API_END_POINT = "&format=json&nojsoncallback=1";
    public static final String PAGE_ID = "page_id";
    public static final String query = "&text=";
    public static final String imageBaseUrl = "https://farm";







    public static ServerResponse getServerStream(String url, String parameter, String reqtype) {

        HttpURLConnection connection = null;

        DataOutputStream printout;
        ServerResponse streamBean = new ServerResponse();
        try {
            connection = (HttpURLConnection) (new URL(url)).openConnection();

            connection.setReadTimeout(20000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod(reqtype);
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Accept", "application/json");
            if (reqtype == NetworkLoader.POST) {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setFixedLengthStreamingMode(parameter.length());
                connection.setUseCaches(false);

                if (parameter != null) {
                    byte[] outputInBytes = parameter.getBytes("UTF-8");
                    OutputStream os = connection.getOutputStream();
                    os.write(outputInBytes);
                    os.close();

                }
            }
            connection.connect();
            //System.out.println("Response Code: " + connection.getResponseCode());
            if (connection.getResponseCode() == 200 && connection.getInputStream() != null) {
                streamBean.setResponseCode(connection.getResponseCode());
                streamBean.setServerResponse(convertStreamToString(connection.getInputStream()));
                return streamBean;
            } else {
                streamBean.setResponseCode(connection.getResponseCode());
                streamBean.setServerResponse(convertStreamToString(connection.getErrorStream()));
                return streamBean;
            }
        } catch (EOFException eof) {
            streamBean.setException(eof);
            eof.printStackTrace();
        } catch (SocketException se) {
            streamBean.setException(se);
            se.printStackTrace();
        } catch (IOException exception) {
            streamBean.setException(exception);
            exception.printStackTrace();
        } catch (Exception exception) {
            streamBean.setException(exception);
            exception.printStackTrace();
        }
        return streamBean;
    }


    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                //FlurryAgent.onError(Definitions.FLURRY_ERROR_NETWORK_OPERATION, e.getMessage(), e.getClass().getName());
                throw new RuntimeException(e.getMessage());
            }
        }
        return sb.toString();
    }

}
