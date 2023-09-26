package timofey.utils.httpServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CustomHttpRequest {
    private HttpResponse response;
    public HttpResponse getBodyFromRequest(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse httpResponse;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            httpResponse = client.send(request,HttpResponse.BodyHandlers.ofString());
            this.response = httpResponse;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return httpResponse;

    }
    public File convertStringToXMLDocument(HttpResponse xmlResponse) {
        String xmlString = xmlResponse.body().toString();
        File xmlDocument = null;
        try {
            xmlDocument = File.createTempFile("tempData", ".xml");
            OutputStream outputStream = new FileOutputStream(xmlDocument);
            outputStream.write(xmlString.getBytes());
        } catch (IOException e) {
            System.out.println("An error occurred while converting XML string to file.");
            e.printStackTrace();
        }
        return xmlDocument;

    }

    public HttpResponse getResponse() {
        return response;
    }
}
