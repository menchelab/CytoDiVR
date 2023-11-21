package univie.menchelab.VRNetzerApp.internal.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.util.swing.OpenBrowser;
import org.json.simple.JSONObject;
import univie.menchelab.VRNetzerApp.internal.util.OpenExternalBrowser;

public class HttpUtil {
    final CyServiceRegistrar registrar;

    public HttpUtil(CyServiceRegistrar registrar) {
        this.registrar = registrar;
    }

    public String performPostRequest(JSONObject jsonData, String url) throws Exception {
        URL URL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
        System.out.println("Initialized Connection");
        // Set up the connection for a POST request
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");


        // Write the JSON data to the request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonData.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Check the HTTP response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            String responseMessage = convertInputStreamToString(inputStream);
            return responseMessage;
        } else {
            throw new RuntimeException("POST request failed. Response Code: " + responseCode);
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        // Read the response line by line
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        // Close the input stream
        inputStream.close();

        // Return the response message as a String
        return stringBuilder.toString();
    }



    public void openUrlInBrowser(String url) {
        // Get the OpenBrowser service
        openUrlInBrowser(url, false);
    }

    public void openUrlInBrowser(String url, boolean external) {
        if (external) { // Get the OpenBrowser service
            OpenExternalBrowser browser = new OpenExternalBrowser();
            try {
                browser.browse(url);
            } catch (IOException | URISyntaxException e) {
                this.openUrlInBrowser(url);
            }
        } else {
            OpenBrowser openBrowserService = this.registrar.getService(OpenBrowser.class);

            // Open the URL in the internal browser
            openBrowserService.openURL(url);
        }

    }
}
