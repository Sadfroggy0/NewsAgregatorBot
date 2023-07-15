package timofey.xmlParser;

import org.springframework.beans.factory.annotation.Autowired;
import timofey.config.SourceInit;
import timofey.entities.NewsArticle;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public abstract class XMLParserByUrl {

    private NewsArticle[] articles;
    private SourceInit rssResources;
    private String httpOutput;
    public XMLParserByUrl(String url, SourceInit rssResources){
        this.rssResources = rssResources;
        String xml = getBodyFromRequest(url);
        this.articles = parseXML(xml);
    }

    //TODO
    public XMLParserByUrl(String[] urls){

    }

    protected abstract NewsArticle[] parseXML(String xml);
    private String getBodyFromRequest(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse httpResponse;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
//                    .version(HttpClient.Version.HTTP_2)
                    .GET()
                    .build();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            httpResponse = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String output =  httpResponse.body().toString();
        this.httpOutput = output;
        return output;
    }


    public String getHttpOutput() {
        return httpOutput;
    }
}
